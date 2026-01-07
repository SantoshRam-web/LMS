package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.FailedLoginAttempt;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.model.UserRole;
import com.lms.www.model.UserSession;
import com.lms.www.repository.FailedLoginAttemptRepository;
import com.lms.www.repository.PasswordResetTokenRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserRoleRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.AuthService;
import com.lms.www.service.EmailService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserSessionRepository userSessionRepository;
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthServiceImpl(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RolePermissionRepository rolePermissionRepository,
            UserSessionRepository userSessionRepository,
            FailedLoginAttemptRepository failedLoginAttemptRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            SystemSettingsRepository systemSettingsRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userSessionRepository = userSessionRepository;
        this.failedLoginAttemptRepository = failedLoginAttemptRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    private SystemSettings getSettings() {
        return systemSettingsRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("System settings not found"));
    }

    // 🔴 TRANSACTION IS MANDATORY (FIX #1)
    @Override
    @Transactional
    public String login(String email, String password, String ipAddress) {

        SystemSettings settings = getSettings();

        User user = userRepository.findByEmail(email).orElse(null);

        // ❌ USER NOT FOUND
        if (user == null) {
            saveFailedAttempt(null, ipAddress);
            throw new RuntimeException("Invalid credentials");
        }

        // ❌ USER DISABLED
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new RuntimeException("Account is disabled");
        }

        // 🔒 CHECK ACCOUNT LOCK
        String lockWindow = LocalDateTime.now()
                .minusMinutes(settings.getAccLockDuration())
                .toString();

        long failedCount = failedLoginAttemptRepository
                .countByUserIdAndAttemptTimeGreaterThan(
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

        // 🔐 PASSWORD EXPIRY CHECK (FIX #2)
        // If reset token EXISTS → password NOT yet reset
        if (passwordResetTokenRepository.findByUser(user).isPresent()) {
            throw new RuntimeException(
                    "Password expired. Please reset your password"
            );
        }

        // ✅ SUCCESS LOGIN → CLEAR FAILED ATTEMPTS (FIX #3)
        failedLoginAttemptRepository.deleteByUserId(user.getUserId());

        // 🔑 ROLES
        List<UserRole> userRoles = userRoleRepository.findByUser(user);

        List<String> roles = userRoles.stream()
                .map(ur -> ur.getRole().getRoleName())
                .toList();

        // 🔑 PERMISSIONS
        Set<String> permissions = new HashSet<>();
        for (UserRole ur : userRoles) {
            rolePermissionRepository.findByRole(ur.getRole())
                    .forEach(rp ->
                            permissions.add(
                                    rp.getPermission().getPermissionName()
                            )
                    );
        }

        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getEmail(),
                roles,
                permissions.stream().toList()
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
        attempt.setAttemptTime(LocalDateTime.now().toString());
        attempt.setIpAddress(ipAddress);
        failedLoginAttemptRepository.save(attempt);
    }
}
