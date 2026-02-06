package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.service.PasswordResetService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/auth/password-reset")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    // 1️⃣ REQUEST OTP
    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOtp(
            @RequestBody(required = false) RequestOtpRequest request,
            HttpServletRequest httpRequest
    ) {
        service.requestPasswordResetOtp(request, httpRequest);
        return ResponseEntity.ok("OTP sent to email");
    }

 // 2️⃣ VERIFY OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestBody VerifyOtpRequest request,
            HttpServletRequest httpRequest
    ) {
        service.verifyPasswordResetOtp(request.getOtp(), httpRequest);
        return ResponseEntity.ok("OTP verified");
    }

    // 3️⃣ CONFIRM PASSWORD
    @PostMapping("/confirm")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        service.confirmPasswordReset(
                request.getNewPassword(),
                request.getConfirmPassword(),
                httpRequest.getRemoteAddr(),
                httpRequest
        );
        return ResponseEntity.ok("Password reset successful");
    }

    // DTOs
    public static class RequestOtpRequest {
        private String email;
        public String getEmail() { return email; }
    }

    static class VerifyOtpRequest {
        private String otp;
        public String getOtp() { return otp; }
    }

    static class ResetPasswordRequest {
        private String newPassword;
        private String confirmPassword;
        public String getNewPassword() { return newPassword; }
        public String getConfirmPassword() { return confirmPassword; }
    }
}



