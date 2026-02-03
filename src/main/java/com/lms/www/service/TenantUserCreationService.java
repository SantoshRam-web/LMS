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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createSuperAdminUserTx(
            String email,
            String rawPassword,
            String firstName,
            String lastName,
            String phone
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setRoleName("ROLE_SUPER_ADMIN");
        user.setEnabled(true);

        user = userRepository.save(user); // ✅ TENANT DB

        SystemSettings settings = new SystemSettings();
        settings.setUserId(user.getUserId());
        settings.setMaxLoginAttempts(5L);
        settings.setAccLockDuration(30L);
        settings.setPassExpiryDays(60L);
        settings.setPassLength(10L); 
        settings.setJwtExpiryMins(60L);
        settings.setSessionTimeout(60L);
        settings.setPasswordLastUpdatedAt(LocalDateTime.now());
        settings.setUpdatedTime(LocalDateTime.now());

        systemSettingsRepository.save(settings);
    }
}
