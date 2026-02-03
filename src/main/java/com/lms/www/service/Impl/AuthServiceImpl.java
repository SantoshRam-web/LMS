package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.model.UserSession;
import com.lms.www.repository.LoginHistoryRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.SuperAdminRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.TenantRegistryRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.AuthService;
import com.lms.www.service.EmailService;
import com.lms.www.service.FailedLoginAttemptService;
import com.lms.www.tenant.TenantContext;
import com.lms.www.tenant.TenantResolver;

import jakarta.servlet.http.HttpServletRequest;

@Service
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
    private final TenantResolver tenantResolver;
    private final SuperAdminRepository superAdminRepository;
    private final TenantRegistryRepository tenantRegistryRepository;
    private final JdbcTemplate jdbcTemplate;

    public AuthServiceImpl(
            UserRepository userRepository,
            RolePermissionRepository rolePermissionRepository,
            UserSessionRepository userSessionRepository,
            SystemSettingsRepository systemSettingsRepository,
            FailedLoginAttemptService failedLoginAttemptService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            LoginHistoryRepository loginHistoryRepository,
            EmailService emailService,
            TenantResolver tenantResolver,
            SuperAdminRepository superAdminRepository,
            TenantRegistryRepository tenantRegistryRepository,
            JdbcTemplate jdbcTemplate
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
        this.tenantResolver = tenantResolver;
        this.superAdminRepository = superAdminRepository;
        this.tenantRegistryRepository = tenantRegistryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String login(
            String email,
            String password,
            String ipAddress,
            HttpServletRequest request
    ) {

        // ================================
        // STEP 1: RESOLVE TENANT (MASTER DB ONLY, NO JPA)
        // ================================

        String tenantDb;

        try {
            // SUPER ADMIN LOGIN (MASTER DB)
            tenantDb = jdbcTemplate.queryForObject(
                    "SELECT tenant_db_name FROM tenant_registry WHERE super_admin_email = ?",
                    String.class,
                    email
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {

            // NON–SUPER ADMIN LOGIN (ADMIN / INSTRUCTOR / STUDENT)
        	tenantDb = request.getHeader("X-Tenant-DB");
        }

        if (tenantDb == null || tenantDb.isBlank()) {
            throw new RuntimeException("Tenant resolution failed");
        }

        // ================================
        // STEP 2: SET TENANT CONTEXT (BEFORE *ANY* JPA CALL)
        // ================================

        TenantContext.setTenant(tenantDb);

        try {
            // ================================
            // STEP 3: TENANT DB OPERATIONS (JPA SAFE)
            // ================================

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            String ipAddress1 = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");

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
                        .recordFailedAttempt(user.getUserId(), ipAddress1);

                emailService.sendLoginFailedMail(
                        user.getEmail(),
                        ipAddress1,
                        userAgent,
                        LocalDateTime.now()
                );

                throw new RuntimeException("Invalid credentials");
            }

            // ✅ SUCCESS
            failedLoginAttemptService.clearAttempts(user.getUserId());

            expireIdleSessions(user, settings);
            validateMultiSession(user, settings);

            settings.setEnableLoginAudit(true);
            systemSettingsRepository.save(settings);

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
                    permissions,
                    tenantDb
            );

            UserSession session = new UserSession();
            session.setUser(user);
            session.setToken(token);
            session.setLoginTime(LocalDateTime.now());
            session.setLastActivityTime(LocalDateTime.now());
            userSessionRepository.save(session);

            return token;

        } finally {
            // ================================
            // STEP 4: ALWAYS CLEAR CONTEXT
            // ================================
            TenantContext.clear();
        }
    }


    /**
     * 🔥 EXPIRE SESSIONS THAT TIMED OUT DUE TO INACTIVITY
     * This prevents login deadlock.
     */
    private void expireIdleSessions(User user, SystemSettings settings) {

        List<UserSession> activeSessions =
                userSessionRepository.findByUserAndLogoutTimeIsNull(user);

        LocalDateTime now = LocalDateTime.now();

        for (UserSession session : activeSessions) {

            LocalDateTime lastActivity =
                    session.getLastActivityTime() != null
                            ? session.getLastActivityTime()
                            : session.getLoginTime();

            LocalDateTime expiryTime =
                    lastActivity.plusMinutes(settings.getSessionTimeout());

            if (now.isAfter(expiryTime)) {
                session.setLogoutTime(now);
                userSessionRepository.save(session);
            }
        }
    }

    /**
     * 🔐 ENFORCE MULTI-SESSION RULE
     */
    private void validateMultiSession(User user, SystemSettings settings) {

        // multi-session users → no restriction
        if (Boolean.TRUE.equals(settings.getMultiSession())) {
            return;
        }

        // single-session users → block if active session exists
        boolean hasActiveSession =
                userSessionRepository
                        .findByUserAndLogoutTimeIsNull(user)
                        .stream()
                        .anyMatch(session -> session.getLogoutTime() == null);

        if (hasActiveSession) {
            throw new RuntimeException(
                    "User already has an active session. Please logout first."
            );
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markLoginFailure(SystemSettings settings) {
        settings.setEnableLoginAudit(false);
        systemSettingsRepository.save(settings);
    }
}
