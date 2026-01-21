package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.OtpVerification;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.repository.OtpVerificationRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.EmailService;
import com.lms.www.service.SuperAdminService;

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

        otpRepo.findByEmailAndPurposeAndVerifiedFalse(
                email, "SUPER_ADMIN_SIGNUP"
        ).ifPresent(o -> {
            throw new RuntimeException("OTP not verified");
        });

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setEnabled(true);
        user.setRoleName("ROLE_SUPER_ADMIN");

        user = userRepository.save(user);

        SystemSettings settings = new SystemSettings();
        settings.setUserId(user.getUserId());
        settings.setMultiSession(true);
        settings.setSessionTimeout(60L);
        settings.setJwtExpiryMins(60L);
        systemSettingsRepository.save(settings);

        String url = user.getFirstName().toLowerCase() + ".yourdomain.com";

        emailService.sendSuperAdminCredentialsMail(
                email, password, url
        );
    }
}
