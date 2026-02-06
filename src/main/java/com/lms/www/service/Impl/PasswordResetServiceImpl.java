package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.JwtUtil;
import com.lms.www.controller.PasswordResetController;
import com.lms.www.model.OtpVerification;
import com.lms.www.model.User;
import com.lms.www.repository.OtpVerificationRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.EmailService;
import com.lms.www.service.PasswordResetService;
import com.lms.www.tenant.TenantContext;
import com.lms.www.tenant.TenantRoutingDataSource;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpRepo;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final JdbcTemplate jdbcTemplate;

    @Qualifier("tenantRoutingDataSource")
    private final DataSource dataSource;

    public PasswordResetServiceImpl(
            UserRepository userRepository,
            OtpVerificationRepository otpRepo,
            SystemSettingsRepository systemSettingsRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JwtUtil jwtUtil,
            JdbcTemplate jdbcTemplate,
            @Qualifier("tenantRoutingDataSource") DataSource dataSource
    ) {
        this.userRepository = userRepository;
        this.otpRepo = otpRepo;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    // 🔥 unwrap routing datasource safely
    private TenantRoutingDataSource routing() {
        if (dataSource instanceof LazyConnectionDataSourceProxy proxy) {
            return (TenantRoutingDataSource) proxy.getTargetDataSource();
        }
        throw new IllegalStateException("TenantRoutingDataSource not found");
    }

    // 🔥 resolve tenant from DOMAIN (NO TOKEN REQUIRED)
    private String resolveTenantDb(HttpServletRequest request) {
        String host = request.getServerName();

        if (host == null || !host.contains(".")) {
            throw new RuntimeException("Invalid tenant domain");
        }

        String subdomain = host.split("\\.")[0].toLowerCase();

        return jdbcTemplate.queryForObject(
                "SELECT tenant_db_name FROM tenant_registry WHERE tenant_domain = ?",
                String.class,
                subdomain
        );
    }

    // ===============================
    // 1️⃣ REQUEST OTP (LOGGED IN / LOGGED OUT)
    // ===============================
    @Override
    public void requestPasswordResetOtp(
            PasswordResetController.RequestOtpRequest request,
            HttpServletRequest httpRequest
    ) {

        String email;
        String tenantDb;

        String auth = httpRequest.getHeader("Authorization");

        // 🔐 LOGGED-IN FLOW
        if (auth != null && auth.startsWith("Bearer ")) {
            email = jwtUtil.extractEmail(auth.substring(7));
            tenantDb = jwtUtil.extractTenantDb(auth.substring(7));
        }
        // 🔓 LOGGED-OUT FLOW (DOMAIN BASED)
        else {
            if (request == null || request.getEmail() == null) {
                throw new RuntimeException("Email required");
            }
            email = request.getEmail();
            tenantDb = resolveTenantDb(httpRequest);
        }

        routing().addTenant(tenantDb);
        TenantContext.setTenant(tenantDb);

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String otp = String.valueOf(100000 + new Random().nextInt(900000));

            OtpVerification entity = new OtpVerification();
            entity.setEmail(email);
            entity.setOtp(otp);
            entity.setPurpose("PASSWORD_RESET");
            entity.setVerified(false);
            entity.setAttempts(0);
            entity.setMaxAttempts(3);
            entity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
            entity.setCreatedAt(LocalDateTime.now());

            otpRepo.save(entity);
            emailService.sendOtpMail(email, otp);

        } finally {
            TenantContext.clear();
        }
    }

    // ===============================
    // 2️⃣ VERIFY OTP (LOGGED OUT)
    // ===============================
    @Override
    public void verifyPasswordResetOtp(String otp, HttpServletRequest request) {

        String tenantDb = resolveTenantDb(request);
        routing().addTenant(tenantDb);
        TenantContext.setTenant(tenantDb);

        try {
            OtpVerification entity = otpRepo
                    .findAll()
                    .stream()
                    .filter(o ->
                            "PASSWORD_RESET".equals(o.getPurpose())
                            && Boolean.FALSE.equals(o.getVerified())
                            && o.getOtp().equals(otp)
                    )
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Invalid OTP"));

            if (LocalDateTime.now().isAfter(entity.getExpiresAt())) {
                throw new RuntimeException("OTP expired");
            }

            entity.setVerified(true);
            otpRepo.save(entity);

        } finally {
            TenantContext.clear();
        }
    }

    // ===============================
    // 3️⃣ CONFIRM PASSWORD (LOGGED OUT)
    // ===============================
    @Override
    public void confirmPasswordReset(
            String newPassword,
            String confirmPassword,
            String ipAddress,
            HttpServletRequest request
    ) {

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        String tenantDb = resolveTenantDb(request);
        routing().addTenant(tenantDb);
        TenantContext.setTenant(tenantDb);

        try {
            OtpVerification otp = otpRepo
                    .findAll()
                    .stream()
                    .filter(o ->
                            "PASSWORD_RESET".equals(o.getPurpose())
                            && Boolean.TRUE.equals(o.getVerified())
                    )
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("OTP not verified"));

            User user = userRepository.findByEmail(otp.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            emailService.sendPasswordResetSuccessMail(
                    user.getEmail(),
                    LocalDateTime.now()
            );

            systemSettingsRepository.findByUserId(user.getUserId())
                    .ifPresent(s -> {
                        s.setPasswordLastUpdatedAt(LocalDateTime.now());
                        systemSettingsRepository.save(s);
                    });

            // 🔥 SINGLE-USE OTP
            otpRepo.delete(otp);

        } finally {
            TenantContext.clear();
        }
    }
}
