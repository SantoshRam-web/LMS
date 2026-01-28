package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.Random;

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

    public PasswordResetServiceImpl(
            UserRepository userRepository,
            OtpVerificationRepository otpRepo,
            SystemSettingsRepository systemSettingsRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.otpRepo = otpRepo;
        this.systemSettingsRepository = systemSettingsRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    // 1️⃣ REQUEST OTP
    @Override
    public void requestPasswordResetOtp(
            PasswordResetController.RequestOtpRequest request,
            HttpServletRequest httpRequest
    ) {
        String email;

        String auth = httpRequest.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            email = jwtUtil.extractEmail(auth.substring(7));
        } else {
            if (request == null || request.getEmail() == null) {
                throw new RuntimeException("Email required");
            }
            email = request.getEmail();
        }

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
    }

    // 2️⃣ VERIFY OTP
    @Override
    public void verifyPasswordResetOtp(String otp) {

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
    }

    // 3️⃣ CONFIRM PASSWORD
    @Override
    public void confirmPasswordReset(
            String newPassword,
            String confirmPassword,
            String ipAddress
    ) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

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
       

    }
}

