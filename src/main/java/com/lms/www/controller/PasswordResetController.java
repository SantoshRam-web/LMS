package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.www.service.PasswordResetService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        passwordResetService.resetPassword(
                request.getUserId(),
                request.getNewPassword(),
                httpRequest.getRemoteAddr()
        );
        return ResponseEntity.ok("Password reset successful");
    }

    static class ResetPasswordRequest {
        private Long userId;
        private String newPassword;

        public Long getUserId() {
            return userId;
        }

        public String getNewPassword() {
            return newPassword;
        }
    }
}


