package com.lms.www.service.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.controller.AdminRequest;
import com.lms.www.model.OtpVerification;
import com.lms.www.model.SuperAdmin;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.repository.OtpVerificationRepository;
import com.lms.www.repository.SuperAdminRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.EmailService;
import com.lms.www.service.SuperAdminService;
import com.lms.www.service.TenantUserCreationService;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    private final OtpVerificationRepository otpRepo;
    private final UserRepository userRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JdbcTemplate jdbcTemplate;
    private final SuperAdminRepository superAdminRepository;
    private final TenantUserCreationService tenantUserCreationService;
    
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
            SuperAdminRepository superAdminRepository,
            TenantUserCreationService tenantUserCreationService
    ) {
        this.otpRepo = otpRepo;
        this.userRepository = userRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jdbcTemplate = jdbcTemplate;
        this.superAdminRepository = superAdminRepository;
        this.tenantUserCreationService = tenantUserCreationService;
    }

    // ================= INIT SIGNUP =================
    @Override
    @Transactional
    public void requestOtp(String email, String phone) {

        if (superAdminRepository.existsByEmail(email)) {
            throw new RuntimeException("Super admin already exists");
        }

        if (superAdminRepository.existsByPhone(phone)) {
            throw new RuntimeException("Phone number already in use");
        }

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

        otpRepo.delete(otp);

        String tenantDb = "lms_tenant_" + System.currentTimeMillis();

        // ✅ CREATE TENANT DB FROM TEMPLATE
        createTenantDatabaseFromTemplate(tenantDb);

        // ✅ REGISTER TENANT (MASTER DB)
        jdbcTemplate.update(
                "INSERT INTO tenant_registry (super_admin_email, tenant_db_name) VALUES (?, ?)",
                email, tenantDb
        );

        // ✅ SAVE SUPER ADMIN (MASTER DB)
        SuperAdmin sa = new SuperAdmin();
        sa.setEmail(email);
        sa.setPhone(phone);
        sa.setPassword(passwordEncoder.encode(password));
        sa.setEnabled(true);
        superAdminRepository.save(sa);

        // ✅ CREATE SUPER ADMIN USER IN TENANT DB
        tenantUserCreationService.createSuperAdminUser(
                tenantDb,
                email,
                password,
                phone
        );

        String url = generateSuperAdminUrl(email);
        emailService.sendSuperAdminCredentialsMail(email, password, url);
    }
    
    // ================= TEMPLATE DB CLONE (FINAL, CORRECT) =================
    private void createTenantDatabaseFromTemplate(String tenantDb) {

        // 1️⃣ Create tenant DB using master connection
        jdbcTemplate.execute(
            "CREATE DATABASE " + tenantDb +
            " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
        );

        try {
            // 2️⃣ Build TENANT JDBC URL (THIS IS THE KEY)
            String masterUrl = jdbcTemplate
                    .getDataSource()
                    .getConnection()
                    .getMetaData()
                    .getURL();

            String tenantUrl = masterUrl.replace("/master_db", "/" + tenantDb);

            // 3️⃣ Open DIRECT connection to tenant DB
            try (Connection tenantConn = DriverManager.getConnection(
                    tenantUrl,
                    dbUser,
                    dbPassword
            )) {
                // 4️⃣ Execute schema INSIDE tenant DB
                ScriptUtils.executeSqlScript(
                        tenantConn,
                        new ClassPathResource("db/tenant_template.sql")
                );
            }

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT
            throw new RuntimeException(
                    "Failed to initialize tenant database schema", e
            );
        }
    }

    private String generateSuperAdminUrl(String email) {
        String localPart = email.split("@")[0]
                .toLowerCase()
                .replaceAll("[^a-z]", "");

        if (localPart.isEmpty()) {
            throw new RuntimeException("Invalid email for URL generation");
        }

        return localPart + ".yourdomain.com";
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
