package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.service.AccountUnlockService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth/account-unlock")
public class AccountUnlockController {

    private final AccountUnlockService service;

    public AccountUnlockController(AccountUnlockService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestUnlock(
            @RequestBody UnlockRequest request,
            HttpServletRequest httpRequest
    ) {
        service.requestUnlock(request.getEmail(), httpRequest);
        return ResponseEntity.ok("Unlock request sent to Super Admin");
    }

    static class UnlockRequest {
        private String email;
        public String getEmail() { return email; }
    }
}

