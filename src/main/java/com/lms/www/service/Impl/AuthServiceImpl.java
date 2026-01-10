package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.FailedLoginAttempt;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.model.UserSession;
import com.lms.www.repository.FailedLoginAttemptRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.AuthService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserSessionRepository userSessionRepository;
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            RolePermissionRepository rolePermissionRepository,
            UserSessionRepository userSessionRepository,
            FailedLoginAttemptRepository failedLoginAttemptRepository,
            SystemSettingsRepository systemSettingsRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userSessionRepository = userSessionRepository;
        this.failedLoginAttemptRepository = failedLoginAttemptRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(String email, String password, String ipAddress) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new RuntimeException("Account is disabled");
        }

        SystemSettings settings = systemSettingsRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("System settings missing"));

        // ===============================
        // 🔒 ACCOUNT LOCK CHECK
        // ===============================
        LocalDateTime lockWindow =
                LocalDateTime.now().minusMinutes(settings.getAccLockDuration());

        long failedCount =
                failedLoginAttemptRepository
                        .countByUserIdAndAttemptTimeAfter(
                                user.getUserId(),
                                lockWindow
                        );

        if (failedCount >= settings.getMaxLoginAttempts()) {
            throw new RuntimeException(
                    "Account locked. Try again after "
                            + settings.getAccLockDuration()
                            + " minutes"
            );
        }

        // ===============================
        // ❌ WRONG PASSWORD
        // ===============================
        if (!passwordEncoder.matches(password, user.getPassword())) {
            saveFailedAttempt(user.getUserId(), ipAddress);
            throw new RuntimeException("Invalid credentials");
        }

        // ===============================
        // ✅ SUCCESS — CLEAR FAILED ATTEMPTS
        // ===============================
        failedLoginAttemptRepository.deleteByUserId(user.getUserId());

        // ===============================
        // 🔐 JWT CREATION
        // ===============================
        List<String> roles = List.of(user.getRoleName());
        List<String> permissions = getPermissions(user);

        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getEmail(),
                roles,
                permissions
        );

        // ===============================
        // 🧾 SESSION TRACKING
        // ===============================
        UserSession session = new UserSession();
        session.setUser(user);
        session.setToken(token);
        session.setLoginTime(LocalDateTime.now());

        userSessionRepository.save(session);

        return token;
    }

    // =====================================================
    // FAILED LOGIN ATTEMPT
    // =====================================================
    private void saveFailedAttempt(Long userId, String ipAddress) {
        FailedLoginAttempt attempt = new FailedLoginAttempt();
        attempt.setUserId(userId);
        attempt.setAttemptTime(LocalDateTime.now());
        attempt.setIpAddress(ipAddress);
        failedLoginAttemptRepository.save(attempt);
    }

    // =====================================================
    // PERMISSIONS (FROM role_permissions USING user_id)
    // =====================================================
    private List<String> getPermissions(User user) {
        return rolePermissionRepository
                .findByUserId(user.getUserId())
                .stream()
                .map(rp -> rp.getPermission().getPermissionName())
                .toList();
    }
}
