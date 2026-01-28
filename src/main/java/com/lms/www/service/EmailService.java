package com.lms.www.service;

import java.time.LocalDateTime;

import com.lms.www.model.User;

public interface EmailService {

    // ACCOUNT
    void sendAccountCredentialsMail(User user, String rawPassword);

    void sendRegistrationMail(User user, String role);

    void sendUserUpdatedMail(User user, String updatedFields, LocalDateTime time);

    void sendRelationMappingMail(User parent, User student, LocalDateTime time);

    // AUTH
    void sendLoginSuccessMail(User user, String ipAddress, LocalDateTime time);

    void sendLoginFailedMail(String email, String ipAddress, String userAgent,LocalDateTime time);

    void sendPasswordResetSuccessMail(
            String email,
            java.time.LocalDateTime resetTime
    );

    
    void sendAccountDeletionMail(User user);
    
    void sendOtpMail(String email, String otp);
    void sendSuperAdminCredentialsMail(
            String email,
            String password,
            String superAdminUrl
    );
    
    void sendAdminCredentialsMail(String email, String password);
    
    void sendNewDeviceLoginAlert(
            User user,
            String ipAddress,
            String userAgent,
            LocalDateTime time
    );

    void sendMultiSessionStatusMail(User user, boolean enabled);
    
    void sendProfileUpdatedMail(User user);
    
    void sendAddressAddedMail(User user);
    void sendAddressUpdatedMail(User user);
    void sendAddressDeletedMail(User user);
    
    void sendParentStudentMappingMailToParent(User parent, User student);
    void sendParentStudentMappingMailToStudent(User student, User parent);
    
    void sendAccountStatusMail(User user, String status);


}
