package com.lms.www.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    public JwtFilter(
            JwtUtil jwtUtil,
            UserRepository userRepository,
            UserSessionRepository userSessionRepository,
            SystemSettingsRepository systemSettingsRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.systemSettingsRepository = systemSettingsRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/")
            || path.startsWith("/super-admin/signup/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            jwtUtil.validateToken(token);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || Boolean.FALSE.equals(user.getEnabled())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserSession session = userSessionRepository
                .findByTokenAndLogoutTimeIsNull(token)
                .orElse(null);

        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // ---------- SESSION TIMEOUT ----------
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

        // ---------- UPDATE ACTIVITY ----------
        session.setLastActivityTime(LocalDateTime.now());
        userSessionRepository.save(session);

        // ---------- AUTHORITIES ----------
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Roles
        List<String> roles = jwtUtil.extractRoles(token);
        if (roles != null) {
            for (String role : roles) {
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }

        // Permissions
        List<String> permissions = jwtUtil.extractPermissions(token);
        if (permissions != null) {
            for (String perm : permissions) {
                authorities.add(new SimpleGrantedAuthority(perm));
            }
        }

        // ---------- AUTH CONTEXT ----------
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}