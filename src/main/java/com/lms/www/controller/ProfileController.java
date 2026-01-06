package com.lms.www.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.model.Address;
import com.lms.www.model.User;
import com.lms.www.service.AddressService;
import com.lms.www.service.AdminService;

@RestController
@RequestMapping("/me")
public class ProfileController {

    private final AdminService adminService;
    private final AddressService addressService;

    public ProfileController(AdminService adminService, AddressService addressService) {
        this.adminService = adminService;
        this.addressService = addressService;
    }

    @GetMapping
    public User myProfile() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return adminService.getUserByEmail(email);
    }

    @GetMapping("/address")
    public Address getMyAddress() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = adminService.getUserByEmail(email);

        return addressService.getAddress(user.getUserId());
    }

}
