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
            @RequestParam Long userId,
            @RequestParam String newPassword,
            HttpServletRequest request
    ) {
        passwordResetService.resetPassword(
                userId,
                newPassword,
                request.getRemoteAddr()
        );
        return ResponseEntity.ok("Password reset successful");
    }
}

