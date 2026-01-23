package com.lms.www.service;

import java.time.LocalDateTime;

import com.lms.www.model.User;

public interface EmailService {

    // ACCOUNT
    void sendAccountCredentialsMail(User user, String rawPassword);

    void sendRegistrationMail(User user, String role);

    void sendUserUpdatedMail(User user, String updatedFields, LocalDateTime time);

    void sendAccountStatusMail(User user, boolean enabled, LocalDateTime time);

    void sendRelationMappingMail(User parent, User student, LocalDateTime time);

    // AUTH
    void sendLoginSuccessMail(User user, String ipAddress, LocalDateTime time);

    void sendLoginFailedMail(String email, String ipAddress, LocalDateTime time);

    void sendPasswordResetMail(User user, LocalDateTime time);
    
    void sendAccountDeletionMail(User user);
    
    void sendOtpMail(String email, String otp);
    void sendSuperAdminCredentialsMail(
            String email,
            String password,
            String superAdminUrl
    );
    
    void sendAdminCredentialsMail(String email, String password);


}
