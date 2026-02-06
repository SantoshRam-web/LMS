package com.lms.www.service;

import com.lms.www.controller.PasswordResetController;
import jakarta.servlet.http.HttpServletRequest;

public interface PasswordResetService {

    void requestPasswordResetOtp(
            PasswordResetController.RequestOtpRequest request,
            HttpServletRequest httpRequest
    );

    void verifyPasswordResetOtp(
            String otp,
            HttpServletRequest httpRequest
    );

    void confirmPasswordReset(
            String newPassword,
            String confirmPassword,
            String ipAddress,
            HttpServletRequest httpRequest
    );
}
