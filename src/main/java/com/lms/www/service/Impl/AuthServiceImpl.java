package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.AuditLog;
import com.lms.www.model.FailedLoginAttempt;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.model.UserSession;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.FailedLoginAttemptRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserRoleRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.AuthService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserSessionRepository userSessionRepository;
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RolePermissionRepository rolePermissionRepository,
            UserSessionRepository userSessionRepository,
            FailedLoginAttemptRepository failedLoginAttemptRepository,
            SystemSettingsRepository systemSettingsRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userSessionRepository = userSessionRepository;
        this.failedLoginAttemptRepository = failedLoginAttemptRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(String email, String password, String ipAddress) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            saveFailedAttempt(null, ipAddress);
            throw new RuntimeException("Invalid credentials");
        }

        SystemSettings settings =
                systemSettingsRepository.findByUserId(user.getUserId())
                        .orElseThrow(() -> new RuntimeException("System settings missing"));

        // 🔒 ACCOUNT LOCK CHECK
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

        // ❌ WRONG PASSWORD
        if (!passwordEncoder.matches(password, user.getPassword())) {
            saveFailedAttempt(user.getUserId(), ipAddress);
            throw new RuntimeException("Invalid credentials");
        }

        // 🔐 PASSWORD EXPIRY CHECK (OPTION A – CORRECT)
        checkPasswordExpiry(user, settings);

        // ✅ SUCCESS — CLEAR FAILED ATTEMPTS
        failedLoginAttemptRepository.deleteByUserId(user.getUserId());

        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getEmail(),
                getRoles(user),
                getPermissions(user)
        );

        UserSession session = new UserSession();
        session.setUser(user);
        session.setToken(token);
        session.setLoginTime(LocalDateTime.now());

        userSessionRepository.save(session);

        return token;
    }

    private void saveFailedAttempt(Long userId, String ipAddress) {
        FailedLoginAttempt attempt = new FailedLoginAttempt();
        attempt.setUserId(userId);
        attempt.setAttemptTime(LocalDateTime.now());
        attempt.setIpAddress(ipAddress);
        failedLoginAttemptRepository.save(attempt);
    }

    private void checkPasswordExpiry(User user, SystemSettings settings) {

        LocalDateTime lastPasswordResetTime =
                auditLogRepository
                        .findTopByEntityNameAndEntityIdAndActionOrderByCreatedTimeDesc(
                                "USER",
                                user.getUserId(),
                                "PASSWORD_RESET"
                        )
                        .map(AuditLog::getCreatedTime)
                        .orElse(null);

        // Never reset → allow login
        if (lastPasswordResetTime == null) {
            return;
        }

        // TEMP: treat days as minutes for testing
        LocalDateTime expiryTime =
                lastPasswordResetTime.plusMinutes(settings.getPassExpiryDays());

        if (LocalDateTime.now().isAfter(expiryTime)) {
            throw new RuntimeException(
                    "Password expired. Reset password to continue"
            );
        }
    }

    private List<String> getRoles(User user) {
        return userRoleRepository.findByUser(user)
                .stream()
                .map(ur -> ur.getRole().getRoleName())
                .toList();
    }

    private List<String> getPermissions(User user) {
        Set<String> permissions = new HashSet<>();

        userRoleRepository.findByUser(user)
                .forEach(ur ->
                        rolePermissionRepository
                                .findByRole(ur.getRole())
                                .forEach(rp ->
                                        permissions.add(
                                                rp.getPermission().getPermissionName()
                                        )
                                )
                );

        return permissions.stream().toList();
    }
}
