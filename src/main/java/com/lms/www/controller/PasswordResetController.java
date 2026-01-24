package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.model.User;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.PasswordResetService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;

    public PasswordResetController(PasswordResetService passwordResetService,
    		UserRepository userRepository) {
        this.passwordResetService = passwordResetService;
        this.userRepository = userRepository;
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
    
    String requesterEmail = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    User requester = userRepository.findByEmail(requesterEmail)
            .orElseThrow(() -> new RuntimeException("Requester not found"));

}


