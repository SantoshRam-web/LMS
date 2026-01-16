package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.User;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.AuthService;
import com.lms.www.service.UserSessionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserSessionService userSessionService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final SystemSettingsRepository systemSettingsRepository;


    public AuthController(
            AuthService authService,
            UserSessionService userSessionService,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            SystemSettingsRepository systemSettingsRepository
    ) {
        this.authService = authService;
        this.userSessionService = userSessionService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.systemSettingsRepository = systemSettingsRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest req,
            HttpServletRequest request
    ) {
        String ipAddress = request.getRemoteAddr();
        return ResponseEntity.ok(
                authService.login(
                        req.getEmail(),
                        req.getPassword(),
                        ipAddress
                )
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing token");
        }

        String token = header.substring(7);

        // 🔐 extract user
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔄 update system_settings
        systemSettingsRepository.findByUserId(user.getUserId())
                .ifPresent(settings -> {
                    settings.setEnableLoginAudit(null); // RESET
                    systemSettingsRepository.save(settings);
                });

        userSessionService.logout(token);

        return ResponseEntity.ok("Logged out successfully");
    }


    // DTO
    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
