package com.lms.www.controller;

import com.lms.www.model.User;
import com.lms.www.service.AdminService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class ProfileController {

    private final AdminService adminService;

    public ProfileController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public User myProfile() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return adminService.getUserByEmail(email);
    }
}
