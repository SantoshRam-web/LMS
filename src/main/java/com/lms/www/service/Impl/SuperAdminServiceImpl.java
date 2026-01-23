package com.lms.www.service.Impl;

import java.time.LocalDateTime;

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

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final OtpVerificationRepository otpRepo;
    private final UserRepository userRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public SuperAdminServiceImpl(
            OtpVerificationRepository otpRepo,
            UserRepository userRepository,
            SystemSettingsRepository systemSettingsRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.otpRepo = otpRepo;
        this.userRepository = userRepository;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
    
    
    // ================= INIT SIGNUP =================
    @Override
    public void requestOtp(String email, String phone) {
    	
    	if (userRepository.existsByPhone(phone)) {
    	    throw new RuntimeException("Phone number already in use");
    	}

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists");
        }

        String otp = String.valueOf(
                100000 + new java.util.Random().nextInt(900000)
        );

        OtpVerification entity =
                otpRepo.findByEmailAndPurpose(email, "SUPER_ADMIN_SIGNUP")
                      .orElse(new OtpVerification());

        entity.setEmail(email);
        entity.setPhone(phone);
        entity.setOtp(otp);
        entity.setPurpose("SUPER_ADMIN_SIGNUP"); // 🔥 FIX
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
    public void verifyOtp(String email, String otp) {

        OtpVerification entity = otpRepo
                .findByEmailAndPurposeAndVerifiedFalse(
                        email, "SUPER_ADMIN_SIGNUP"
                )
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

        // 🔒 1️⃣ OTP MUST EXIST AND MUST BE VERIFIED
        OtpVerification otp = otpRepo
                .findByEmailAndPurpose(email, "SUPER_ADMIN_SIGNUP")
                .orElseThrow(() ->
                        new RuntimeException("OTP verification required")
                );

        if (!Boolean.TRUE.equals(otp.getVerified())) {
            throw new RuntimeException("OTP not verified");
        }

        // 🔒 2️⃣ Prevent reuse of same OTP
        otpRepo.delete(otp);

        // 🔒 3️⃣ Create Super Admin user
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setEnabled(true);
        user.setRoleName("ROLE_SUPER_ADMIN");

        user = userRepository.save(user);

        // 🔒 4️⃣ Create system settings (ALL mandatory fields)
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

        // 🔒 5️⃣ Send credentials mail
        String url = generateSuperAdminUrl(user.getEmail());
        emailService.sendSuperAdminCredentialsMail(email, password, url);
    }
    
    private String generateSuperAdminUrl(String email) {

        // take part before @
        String localPart = email.split("@")[0];

        // keep ONLY characters (remove numbers + special chars)
        localPart = localPart
                .toLowerCase()
                .replaceAll("[^a-z]", "");

        if (localPart.isEmpty()) {
            throw new RuntimeException("Invalid email for URL generation");
        }

        return localPart + ".yourdomain.com";
    }
    
    @Override
    public void createAdmin(
            AdminRequest adminRequest,
            HttpServletRequest httpRequest
    ) {

        // 1️⃣ Email uniqueness check
        if (userRepository.existsByEmail(adminRequest.getEmail())) {
            throw new RuntimeException("User already exists with this email");
        }

        // 2️⃣ Create ADMIN user
        User admin = new User();
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(
                passwordEncoder.encode(adminRequest.getPassword())
        );
        admin.setFirstName(adminRequest.getFirstName());
        admin.setLastName(adminRequest.getLastName());
        admin.setPhone(adminRequest.getPhone());
        admin.setEnabled(true);
        admin.setRoleName("ROLE_ADMIN");

        admin = userRepository.save(admin);

        // 3️⃣ Create SYSTEM SETTINGS (ALL REQUIRED FIELDS)
        SystemSettings settings = new SystemSettings();
        settings.setUserId(admin.getUserId());

        settings.setMaxLoginAttempts(5L);
        settings.setAccLockDuration(30L);
        settings.setPassExpiryDays(60L);
        settings.setPassLength(10L);

        settings.setJwtExpiryMins(60L);
        settings.setSessionTimeout(60L);

        settings.setMultiSession(false);
        settings.setEnableLoginAudit(null);
        settings.setEnableAuditLog(null);

        settings.setPasswordLastUpdatedAt(LocalDateTime.now());
        settings.setUpdatedTime(LocalDateTime.now());

        systemSettingsRepository.save(settings);

        // 4️⃣ Send credentials mail
        emailService.sendAdminCredentialsMail(
                admin.getEmail(),
                adminRequest.getPassword()
        );
    }

 
}
