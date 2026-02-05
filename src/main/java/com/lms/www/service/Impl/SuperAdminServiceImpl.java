package com.lms.www.service.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.controller.AdminRequest;
import com.lms.www.model.OtpVerification;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.repository.OtpVerificationRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.EmailService;
import com.lms.www.service.SuperAdminService;
import com.lms.www.service.TenantUserCreationService;
import com.lms.www.tenant.TenantContext;
import com.lms.www.tenant.TenantResolver;
import com.lms.www.tenant.TenantRoutingDataSource;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    private final OtpVerificationRepository otpRepo;
    private final UserRepository userRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JdbcTemplate jdbcTemplate;
    private final TenantUserCreationService tenantUserCreationService;
    private final DataSource tenantRoutingDataSource;
    private final TenantResolver tenantResolver;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public SuperAdminServiceImpl(
            OtpVerificationRepository otpRepo,
            UserRepository userRepository,
            SystemSettingsRepository systemSettingsRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JdbcTemplate jdbcTemplate,
            TenantUserCreationService tenantUserCreationService,
            TenantResolver tenantResolver,
            @Qualifier("tenantRoutingDataSource") DataSource tenantRoutingDataSource
    ) {
        this.otpRepo = otpRepo;
        this.userRepository = userRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jdbcTemplate = jdbcTemplate;
        this.tenantUserCreationService = tenantUserCreationService;
        this.tenantRoutingDataSource = tenantRoutingDataSource;
        this.tenantResolver = tenantResolver;
    }

    // ================= INIT SIGNUP =================
    @Override
    @Transactional
    public void requestOtp(String email, String phone) {

        String otp = String.valueOf(100000 + new java.util.Random().nextInt(900000));

        OtpVerification entity =
                otpRepo.findByEmailAndPurpose(email, "SUPER_ADMIN_SIGNUP")
                       .orElse(new OtpVerification());

        entity.setEmail(email);
        entity.setPhone(phone);
        entity.setOtp(otp);
        entity.setPurpose("SUPER_ADMIN_SIGNUP");
        entity.setAttempts(0);
        entity.setMaxAttempts(3);
        entity.setVerified(false);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        entity.setCreatedAt(LocalDateTime.now());

        otpRepo.save(entity);
        emailService.sendOtpMail(email, otp);
    }

    // ================= VERIFY OTP =================
    @Override
    @Transactional
    public void verifyOtp(String email, String otp) {

        OtpVerification entity = otpRepo
                .findByEmailAndPurposeAndVerifiedFalse(email, "SUPER_ADMIN_SIGNUP")
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (LocalDateTime.now().isAfter(entity.getExpiresAt())) {
            throw new RuntimeException("OTP expired");
        }

        if (!entity.getOtp().equals(otp)) {
            entity.setAttempts(entity.getAttempts() + 1);
            otpRepo.save(entity);
            throw new RuntimeException("Invalid OTP");
        }

        entity.setVerified(true);
        otpRepo.save(entity);
    }

    // ================= FINAL SIGNUP =================
    @Override
    public void signupSuperAdmin(
            String email,
            String password,
            String firstName,
            String lastName,
            String phone
    ) {

        OtpVerification otp = otpRepo
                .findByEmailAndPurpose(email, "SUPER_ADMIN_SIGNUP")
                .orElseThrow(() -> new RuntimeException("OTP verification required"));

        if (!otp.getVerified()) {
            throw new RuntimeException("OTP not verified");
        }

        otpRepo.delete(otp); // master DB – OK

        String tenantDb = "lms_tenant_" + System.currentTimeMillis();

        // 1️⃣ CREATE TENANT DB
        createTenantDatabaseFromTemplate(tenantDb);

        // 2️⃣ REGISTER TENANT DATASOURCE
        ((TenantRoutingDataSource)
            ((LazyConnectionDataSourceProxy) tenantRoutingDataSource).getTargetDataSource()
        ).addTenant(tenantDb);

        // 🔥 IMPORTANT: ONLY subdomain
        String tenantDomain = generateSuperAdminUrl(email); // e.g. "santoshchavithini"

        // 3️⃣ REGISTER TENANT (MASTER DB ONLY)
        jdbcTemplate.update(
            "INSERT INTO tenant_registry (super_admin_email, tenant_db_name, tenant_domain) VALUES (?,?,?)",
            email,
            tenantDb,
            tenantDomain
        );

        // 4️⃣ SWITCH TO TENANT DB
        TenantContext.setTenant(tenantDb);
        try {
            tenantUserCreationService.createSuperAdminUserTx(
                    email,
                    password,
                    firstName,
                    lastName,
                    phone
            );
        } finally {
            TenantContext.clear();
        }

        // 🔥 Build FULL URL only for email
        String loginUrl = tenantResolver.buildTenantLoginUrl(tenantDomain);
        emailService.sendSuperAdminCredentialsMail(email, password, loginUrl);
    }


    // ================= TEMPLATE DB CLONE =================
    private void createTenantDatabaseFromTemplate(String tenantDb) {

        jdbcTemplate.execute(
            "CREATE DATABASE " + tenantDb +
            " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
        );

        try {
            String masterUrl = jdbcTemplate
                    .getDataSource()
                    .getConnection()
                    .getMetaData()
                    .getURL();

            String tenantUrl = masterUrl.replace("/master_db", "/" + tenantDb);

            try (Connection tenantConn = DriverManager.getConnection(
                    tenantUrl,
                    dbUser,
                    dbPassword
            )) {
                ScriptUtils.executeSqlScript(
                        tenantConn,
                        new ClassPathResource("db/tenant_template.sql")
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize tenant database schema", e);
        }
    }

    private String generateSuperAdminUrl(String email) {
        String localPart = email.split("@")[0]
                .toLowerCase()
                .replaceAll("[^a-z]", "");

        if (localPart.isEmpty()) {
            throw new RuntimeException("Invalid email for URL generation");
        }

        // ONLY return the URL (do NOT touch DB here)
        return localPart;
    }


    // ================= CREATE ADMIN =================
    @Override
    @Transactional
    public void createAdmin(AdminRequest adminRequest, HttpServletRequest httpRequest) {

        if (userRepository.existsByEmail(adminRequest.getEmail())) {
            throw new RuntimeException("User already exists with this email");
        }

        User admin = new User();
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        admin.setFirstName(adminRequest.getFirstName());
        admin.setLastName(adminRequest.getLastName());
        admin.setPhone(adminRequest.getPhone());
        admin.setEnabled(true);
        admin.setRoleName("ROLE_ADMIN");

        admin = userRepository.save(admin);

        SystemSettings settings = new SystemSettings();
        settings.setUserId(admin.getUserId());
        settings.setMaxLoginAttempts(5L);
        settings.setAccLockDuration(30L);
        settings.setPassExpiryDays(60L);
        settings.setPassLength(10L);
        settings.setJwtExpiryMins(60L);
        settings.setSessionTimeout(60L);
        settings.setMultiSession(false);
        settings.setPasswordLastUpdatedAt(LocalDateTime.now());
        settings.setUpdatedTime(LocalDateTime.now());

        systemSettingsRepository.save(settings);

        emailService.sendAdminCredentialsMail(
                admin.getEmail(),
                adminRequest.getPassword()
        );
    }
}
