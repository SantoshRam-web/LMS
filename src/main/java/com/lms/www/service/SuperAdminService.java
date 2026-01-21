package com.lms.www.service;

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
}
