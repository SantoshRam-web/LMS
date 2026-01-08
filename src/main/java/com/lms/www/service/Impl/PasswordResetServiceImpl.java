package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.AuditLog;
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

        SystemSettings settings =
                systemSettingsRepository.findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException("System settings not found"));

        if (newPassword.length() < settings.getPassLength()) {
            throw new RuntimeException(
                    "Password must be at least " +
                            settings.getPassLength() + " characters"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // ❗ DO NOT DELETE reset token (history retained)

        if (Boolean.TRUE.equals(settings.getEnableAuditLog())) {
            AuditLog log = new AuditLog();
            log.setAction("PASSWORD_RESET");
            log.setEntityName("USER");
            log.setEntityId(user.getUserId());
            log.setPerformedBy(user);
            log.setCreatedTime(LocalDateTime.now());
            log.setIpAddress(ipAddress);

            auditLogRepository.save(log);
        }
    }
}
