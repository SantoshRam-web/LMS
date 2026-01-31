package com.lms.www.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.tenant.TenantContext;

@Service
public class TenantUserCreationService {

    private final UserRepository userRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;

    public TenantUserCreationService(
            UserRepository userRepository,
            SystemSettingsRepository systemSettingsRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createSuperAdminUser(
            String tenantDb,
            String email,
            String rawPassword,
            String phone
    ) {
        TenantContext.setTenant(tenantDb);
        try {
            createUserInternal(email, rawPassword, phone);
        } finally {
            TenantContext.clear();
        }
    }

    @Transactional
    protected void createUserInternal(
            String email,
            String rawPassword,
            String phone
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPhone(phone);
        user.setRoleName("ROLE_SUPER_ADMIN");
        user.setEnabled(true);

        user = userRepository.save(user);

        SystemSettings settings = new SystemSettings();
        settings.setUserId(user.getUserId());
        settings.setMaxLoginAttempts(5L);
        settings.setAccLockDuration(30L);
        settings.setPassExpiryDays(60L);
        settings.setPassLength(10L);
        settings.setJwtExpiryMins(60L);
        settings.setSessionTimeout(60L);
        settings.setMultiSession(true);
        settings.setPasswordLastUpdatedAt(LocalDateTime.now());
        settings.setUpdatedTime(LocalDateTime.now());

        systemSettingsRepository.save(settings);
    }
}

