package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.UserAuthorizationUtil;
import com.lms.www.model.AuditLog;
import com.lms.www.model.PasswordResetTokens;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.PasswordResetTokenRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.EmailService;
import com.lms.www.service.PasswordResetService;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationContext applicationContext;
    private final EmailService emailService;

    public PasswordResetServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            SystemSettingsRepository systemSettingsRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder,
            ApplicationContext applicationContext,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationContext = applicationContext;
        this.emailService = emailService;
    }

    @Override
    public void resetPassword(Long userId, String newPassword, String ipAddress) {

        SystemSettings settings = systemSettingsRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("System settings not found"));

        try {
            if (newPassword.length() < settings.getPassLength()) {
                throw new RuntimeException(
                        "Password must be at least " + settings.getPassLength() + " characters"
                );
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            String requesterEmail = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User requester = userRepository.findByEmail(requesterEmail)
                    .orElseThrow(() -> new RuntimeException("Requester not found"));

            // 🔒 BLOCK NON-SUPER-ADMIN → SUPER-ADMIN
            UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(
                    requester,
                    user
            );

            // 🔐 update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            emailService.sendPasswordResetMail(user, LocalDateTime.now());


            // 🔑 reset expiry timer
            settings.setPasswordLastUpdatedAt(LocalDateTime.now());
            settings.setUpdatedTime(LocalDateTime.now());
            settings.setEnableAuditLog(true);   // ✅ SUCCESS
            systemSettingsRepository.save(settings);

            // 🧾 password reset token
            PasswordResetTokens token = new PasswordResetTokens();
            token.setUser(user);
            token.setResetToken("MANUAL_RESET_" + System.currentTimeMillis());
            token.setCreatedTime(LocalDateTime.now());
            passwordResetTokenRepository.save(token);
            
            emailService.sendRegistrationMail(user, "PASSWORD RESET");


            // ✅ audit log
            AuditLog log = new AuditLog();
            log.setAction("PASSWORD_RESET");
            log.setEntityName("USER");
            log.setEntityId(user.getUserId());
            log.setUserId(user.getUserId());
            log.setCreatedTime(LocalDateTime.now());
            log.setIpAddress(ipAddress);
            auditLogRepository.save(log);

        } catch (RuntimeException ex) {

            // ❌ FAILURE — MUST COMMIT
            proxy().markAuditFailure(userId);

            throw ex;
        }
    }

    // 🔴 FAILURE MUST RUN IN NEW TRANSACTION
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAuditFailure(Long userId) {

        SystemSettings settings = systemSettingsRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("System settings not found"));

        settings.setEnableAuditLog(false);
        systemSettingsRepository.save(settings);
    }

    private PasswordResetServiceImpl proxy() {
        return applicationContext.getBean(PasswordResetServiceImpl.class);
    }
}
