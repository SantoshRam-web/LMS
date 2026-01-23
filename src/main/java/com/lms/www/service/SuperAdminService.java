package com.lms.www.service;

import com.lms.www.controller.AdminRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface SuperAdminService {

    void requestOtp(String email, String phone);

    void verifyOtp(String email, String otp);

    void signupSuperAdmin(
            String email,
            String password,
            String firstName,
            String lastName,
            String phone
    );
    

    void createAdmin(
            AdminRequest request,
            HttpServletRequest httpRequest
    );

}
