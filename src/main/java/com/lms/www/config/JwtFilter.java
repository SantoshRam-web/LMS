package com.lms.www.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.model.UserSession;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.tenant.TenantContext;
import com.lms.www.tenant.TenantRoutingDataSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("tenantRoutingDataSource")
    private DataSource dataSource;

    public JwtFilter(
            JwtUtil jwtUtil,
            UserRepository userRepository,
            UserSessionRepository userSessionRepository,
            SystemSettingsRepository systemSettingsRepository,
            JdbcTemplate jdbcTemplate
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private TenantRoutingDataSource routing() {
        if (dataSource instanceof LazyConnectionDataSourceProxy proxy) {
            return (TenantRoutingDataSource) proxy.getTargetDataSource();
        }
        throw new IllegalStateException("TenantRoutingDataSource not found");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/password-reset/")
                || path.startsWith("/super-admin/signup/")
                || path.startsWith("/super-admin/request-disable")
                || path.startsWith("/platform/");
    }

    private String extractSubdomain(HttpServletRequest request) {
        String host = request.getServerName();
        if (host == null || !host.contains(".")) {
            return null;
        }
        return host.split("\\.")[0].toLowerCase();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean isLoginRequest = path.startsWith("/auth/login");

        try {
            // ================================
            // 1️⃣ Extract subdomain
            // ================================
            String subdomain = extractSubdomain(request);

            // ================================
            // 2️⃣ TENANT ENABLE CHECK (MASTER DB)
            // ================================
            if (subdomain != null) {
                Boolean tenantEnabled;
                try {
                    tenantEnabled = jdbcTemplate.queryForObject(
                            "SELECT enabled FROM tenant_registry WHERE tenant_domain = ?",
                            Boolean.class,
                            subdomain
                    );
                } catch (Exception ex) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (Boolean.FALSE.equals(tenantEnabled)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }

            // ================================
            // 3️⃣ TOKEN HANDLING
            // ================================
            String authHeader = request.getHeader("Authorization");
            String token = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            // 🔐 For protected APIs, token is mandatory
            if (!isLoginRequest && token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // ================================
            // 4️⃣ JWT VALIDATION (ONLY IF TOKEN PRESENT)
            // ================================
            if (token != null) {
                try {
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

            // ================================
            // 5️⃣ LOGIN REQUEST → CONTINUE
            // ================================
            if (isLoginRequest) {
                filterChain.doFilter(request, response);
                return;
            }

            // ================================
            // 6️⃣ EXTRACT TENANT DB FROM JWT
            // ================================
            String tenantDb = jwtUtil.extractTenantDb(token);
            if (tenantDb == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // ================================
            // 7️⃣ DOMAIN ↔ TENANT VALIDATION
            // ================================
            if (subdomain != null) {
                String expectedTenantDb;
                try {
                    expectedTenantDb = jdbcTemplate.queryForObject(
                            "SELECT tenant_db_name FROM tenant_registry WHERE tenant_domain = ?",
                            String.class,
                            subdomain
                    );
                } catch (Exception ex) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (!tenantDb.equals(expectedTenantDb)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }

            // ================================
            // 8️⃣ SWITCH TO TENANT DB
            // ================================
            routing().addTenant(tenantDb);
            TenantContext.setTenant(tenantDb);

            // ================================
            // 9️⃣ LOAD USER
            // ================================
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null || Boolean.FALSE.equals(user.getEnabled())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // ================================
            // 🔟 VALIDATE SESSION
            // ================================
            UserSession session = userSessionRepository
                    .findByTokenAndLogoutTimeIsNull(token)
                    .orElse(null);

            if (session == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // ================================
            // 1️⃣1️⃣ SESSION TIMEOUT CHECK
            // ================================
            SystemSettings settings = systemSettingsRepository
                    .findByUserId(user.getUserId())
                    .orElse(null);

            if (settings != null && settings.getSessionTimeout() != null) {
                LocalDateTime lastActivity =
                        session.getLastActivityTime() != null
                                ? session.getLastActivityTime()
                                : session.getLoginTime();

                long idleMinutes = ChronoUnit.MINUTES.between(
                        lastActivity,
                        LocalDateTime.now()
                );

                if (idleMinutes >= settings.getSessionTimeout()) {
                    session.setLogoutTime(LocalDateTime.now());
                    userSessionRepository.save(session);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

            // ================================
            // 1️⃣2️⃣ UPDATE ACTIVITY
            // ================================
            session.setLastActivityTime(LocalDateTime.now());
            userSessionRepository.save(session);

            // ================================
            // 1️⃣3️⃣ BUILD AUTHORITIES
            // ================================
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            List<String> roles = jwtUtil.extractRoles(token);
            if (roles != null) {
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            }

            List<String> permissions = jwtUtil.extractPermissions(token);
            if (permissions != null) {
                for (String perm : permissions) {
                    authorities.add(new SimpleGrantedAuthority(perm));
                }
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            null,
                            authorities
                    );

            authentication.setDetails(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }
}
