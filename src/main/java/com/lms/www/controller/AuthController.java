package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.service.AuthService;
import com.lms.www.service.UserSessionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserSessionService userSessionService;

    public AuthController(
            AuthService authService,
            UserSessionService userSessionService
    ) {
        this.authService = authService;
        this.userSessionService = userSessionService;
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
