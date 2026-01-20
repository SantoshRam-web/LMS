package com.lms.www.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (jwtUtil.validateToken(token)) {

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

                SystemSettings settings = systemSettingsRepository
                        .findByUserId(user.getUserId())
                        .orElse(null);

                if (settings != null && settings.getSessionTimeout() != null) {

                    LocalDateTime lastActivity =
                            session.getLastActivityTime() != null
                                    ? session.getLastActivityTime()
                                    : session.getLoginTime();

                    long idleMinutes =
                            ChronoUnit.MINUTES.between(
                                    lastActivity,
                                    LocalDateTime.now()
                            );

                    // ⛔ SESSION TIMEOUT
                    if (idleMinutes >= settings.getSessionTimeout()) {
                        session.setLogoutTime(LocalDateTime.now());
                        userSessionRepository.save(session);

                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Session expired due to inactivity");
                        return;
                    }
                }

                // ✅ UPDATE ACTIVITY
                session.setLastActivityTime(LocalDateTime.now());
                userSessionRepository.save(session);

                List<String> roles = jwtUtil.extractRoles(token);
                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
