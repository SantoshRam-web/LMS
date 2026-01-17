package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.AuditLog;
import com.lms.www.model.PasswordResetTokens;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.PasswordResetTokenRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.PasswordResetService;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            SystemSettingsRepository systemSettingsRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void resetPassword(Long userId, String newPassword, String ipAddress) {

        try {
            SystemSettings settings = systemSettingsRepository
                    .findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("System settings not found"));

            if (newPassword.length() < settings.getPassLength()) {
                throw new RuntimeException(
                        "Password must be at least " + settings.getPassLength() + " characters"
                );
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 🔐 update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // 🔑 RESET PASSWORD TIMER
            settings.setPasswordLastUpdatedAt(LocalDateTime.now());
            settings.setUpdatedTime(LocalDateTime.now());
            systemSettingsRepository.save(settings);

            // 🧾 Password reset token history (REQUIRED)
            PasswordResetTokens token = new PasswordResetTokens();
            token.setUser(user);
            token.setResetToken("MANUAL_RESET_" + System.currentTimeMillis());
            token.setCreatedTime(LocalDateTime.now());
            passwordResetTokenRepository.save(token);

            // ✅ SUCCESS → update audit flag FIRST
            settings.setEnableAuditLog(true);
            systemSettingsRepository.save(settings);

            // ✅ THEN write audit log
            AuditLog log = new AuditLog();
            log.setAction("PASSWORD_RESET");
            log.setEntityName("USER");
            log.setEntityId(user.getUserId());
            log.setUserId(user.getUserId());
            log.setCreatedTime(LocalDateTime.now());
            log.setIpAddress(ipAddress);
            auditLogRepository.save(log);

        } catch (RuntimeException ex) {

            // ❌ FAILURE → update audit flag ONLY
            systemSettingsRepository.findByUserId(userId)
                    .ifPresent(settings -> {
                        settings.setEnableAuditLog(false);
                        systemSettingsRepository.save(settings);
                    });

            throw ex;
        }
    }
}
