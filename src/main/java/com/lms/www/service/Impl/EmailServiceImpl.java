package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lms.www.model.User;
import com.lms.www.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ================= ACCOUNT CREATION =================
    @Override
    public void sendAccountCredentialsMail(User user, String rawPassword) {
        send(
            user.getEmail(),
            "Your LMS Login Credentials",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your LMS account has been created.\n\n" +
            "Login Email: " + user.getEmail() + "\n" +
            "Password: " + rawPassword + "\n\n" +
            "Please change your password after first login.\n\n" +
            "- LMS Team"
        );
    }

    @Override
    public void sendRegistrationMail(User user, String role) {
        send(
            user.getEmail(),
            "LMS Account Created",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your LMS account is active.\n" +
            "Role: " + role + "\n\n" +
            "- LMS Team"
        );
    }

    // ================= USER UPDATE =================
    @Override
    public void sendUserUpdatedMail(User user, String updatedFields, LocalDateTime time) {
        send(
            user.getEmail(),
            "Profile Updated",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your profile was updated.\n\n" +
            "Changes: " + updatedFields + "\n" +
            "Time: " + time + "\n\n" +
            "- LMS Security"
        );
    }

    @Override
    public void sendAccountStatusMail(User user, boolean enabled, LocalDateTime time) {
        send(
            user.getEmail(),
            "Account Status Changed",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your account has been " + (enabled ? "ENABLED" : "DISABLED") + ".\n\n" +
            "Time: " + time + "\n\n" +
            "- LMS Admin"
        );
    }

    @Override
    public void sendRelationMappingMail(User parent, User student, LocalDateTime time) {
        send(
            parent.getEmail(),
            "Student Mapped",
            "Hello " + parent.getFirstName() + ",\n\n" +
            "Student " + student.getFirstName() + " has been mapped to you.\n\n" +
            "Time: " + time + "\n\n" +
            "- LMS Admin"
        );
    }

    // ================= AUTH =================
    @Override
    public void sendLoginSuccessMail(User user, String ipAddress, LocalDateTime time) {
        send(
            user.getEmail(),
            "Login Successful",
            "Login detected.\n\nIP: " + ipAddress + "\nTime: " + time
        );
    }

    @Override
    public void sendLoginFailedMail(String email, String ipAddress, LocalDateTime time) {
        send(
            email,
            "Failed Login Attempt",
            "Failed login attempt detected.\n\nIP: " + ipAddress + "\nTime: " + time
        );
    }

    @Override
    public void sendPasswordResetMail(User user, LocalDateTime time) {
        send(
            user.getEmail(),
            "Password Reset",
            "Your password was reset successfully.\n\nTime: " + time
        );
    }
    

    // ================= COMMON =================
    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("MAIL ERROR: " + e.getMessage());
        }

    }
    
    @Override
    public void sendAccountDeletionMail(User user) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("LMS Account Deleted");
            msg.setText(
                    "Hello " + user.getFirstName() + ",\n\n" +
                    "Your LMS account has been deleted by an administrator.\n\n" +
                    "If this was not expected, please contact support.\n\n" +
                    "- LMS Team"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("MAIL ERROR (account delete): " + e.getMessage());
        }
    }

    
    
}
