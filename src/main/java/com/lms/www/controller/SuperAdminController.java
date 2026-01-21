package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.service.SuperAdminService;

@RestController
@RequestMapping("/super-admin")
public class SuperAdminController {

    private final SuperAdminService service;

    public SuperAdminController(SuperAdminService service) {
        this.service = service;
    }

    // 🔹 INIT SIGNUP → SEND OTP
    @PostMapping("/signup/init")
    public ResponseEntity<String> initSignup(
            @RequestBody SignupRequest request
    ) {
        service.requestOtp(
                request.getEmail(),
                request.getPhone()
        );
        return ResponseEntity.ok("OTP sent to email");
    }

    // 🔹 VERIFY OTP
    @PostMapping("/signup/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        service.verifyOtp(email, otp);
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequest request
    ) {
        service.signupSuperAdmin(
            request.getEmail(),
            request.getPassword(),
            request.getFirstName(),
            request.getLastName(),
            request.getPhone()
        );
        return ResponseEntity.ok("Super Admin created");
    }

    static class SignupRequest {
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phone;

        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
    }

}
