package com.lms.www.service;

public interface PasswordResetService {

    void resetPassword(
            Long userId,
            String newPassword,
            String ipAddress
    );
}
