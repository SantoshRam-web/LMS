package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.LoginHistory;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.model.UserSession;
import com.lms.www.repository.LoginHistoryRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.AuthService;
import com.lms.www.service.EmailService;
import com.lms.www.service.FailedLoginAttemptService;

@Service
@Transactional(noRollbackFor = RuntimeException.class)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserSessionRepository userSessionRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final FailedLoginAttemptService failedLoginAttemptService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final LoginHistoryRepository loginHistoryRepository;
    private final EmailService emailService;


    public AuthServiceImpl(
            UserRepository userRepository,
            RolePermissionRepository rolePermissionRepository,
            UserSessionRepository userSessionRepository,
            SystemSettingsRepository systemSettingsRepository,
            FailedLoginAttemptService failedLoginAttemptService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            LoginHistoryRepository loginHistoryRepository,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userSessionRepository = userSessionRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.failedLoginAttemptService = failedLoginAttemptService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.loginHistoryRepository = loginHistoryRepository;
        this.emailService = emailService;
    }

    @Override
    public String login(String email, String password, String ipAddress) {

        User user = userRepository.findByEmail(email).orElse(null);

        // ❌ USER NOT FOUND
        if (user == null) {
            failedLoginAttemptService.recordFailedAttempt(null, ipAddress);
            throw new RuntimeException("Invalid credentials");
        }

        // ⛔ DISABLED USER
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new RuntimeException("User account is disabled");
        }

        SystemSettings settings = systemSettingsRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("System settings missing"));

        // 🔐 PASSWORD EXPIRY CHECK
        if (settings.getPasswordLastUpdatedAt() != null
                && settings.getPassExpiryDays() != null) {

            LocalDateTime expiryTime =
                    settings.getPasswordLastUpdatedAt()
                    .plusDays(settings.getPassExpiryDays());

            if (LocalDateTime.now().isAfter(expiryTime)) {
                throw new RuntimeException(
                        "Password expired. Please reset your password."
                );
            }
        }


        // 🔒 ACCOUNT LOCK CHECK
        long attempts =
                failedLoginAttemptService.countRecentAttempts(
                        user.getUserId(),
                        settings.getAccLockDuration()
                );

        if (attempts >= settings.getMaxLoginAttempts()) {
            throw new RuntimeException(
                    "Account locked. Try again after "
                            + settings.getAccLockDuration()
                            + " minutes"
            );
        }

        // ❌ WRONG PASSWORD
        if (!passwordEncoder.matches(password, user.getPassword())) {
            settings.setEnableLoginAudit(false);
            systemSettingsRepository.save(settings);

            failedLoginAttemptService
                    .recordFailedAttempt(user.getUserId(), ipAddress);
            
            emailService.sendLoginFailedMail(
            	    user.getEmail(),
            	    ipAddress,
            	    LocalDateTime.now()
            	);

            throw new RuntimeException("Invalid credentials");
        }
        
        

        // ✅ LOGIN SUCCESS
        failedLoginAttemptService.clearAttempts(user.getUserId());

        settings.setEnableLoginAudit(true);
        systemSettingsRepository.save(settings);

        // ✅ LOGIN HISTORY
        LoginHistory history = new LoginHistory();
        history.setUser(user);
        history.setIpAddress(ipAddress);
        history.setDevice("PostmanRuntime");
        history.setLoginTime(LocalDateTime.now());
        loginHistoryRepository.save(history);
        
        emailService.sendLoginSuccessMail(
        	    user,
        	    ipAddress,
        	    LocalDateTime.now()
        	);


        List<String> permissions =
        		rolePermissionRepository.findByRoleName(user.getRoleName())
                        .stream()
                        .map(rp -> rp.getPermission().getPermissionName())
                        .distinct()
                        .toList();

        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getEmail(),
                List.of(user.getRoleName()),
                permissions
        );

        UserSession session = new UserSession();
        session.setUser(user);
        session.setToken(token);
        session.setLoginTime(LocalDateTime.now());
        userSessionRepository.save(session);

        return token;
    }


    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markLoginFailure(SystemSettings settings) {
        settings.setEnableLoginAudit(false);
        systemSettingsRepository.save(settings);
    }

}
