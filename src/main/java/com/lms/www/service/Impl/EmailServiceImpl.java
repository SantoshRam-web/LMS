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

    @Override
    public void sendRegistrationMail(User user, String role) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("LMS Account Created");
            msg.setText(
                    "Hello " + user.getFirstName() + ",\n\n" +
                    "Your LMS account has been created successfully.\n\n" +
                    "Role: " + role + "\n" +
                    "Email: " + user.getEmail() + "\n\n" +
                    "Please login using your credentials.\n\n" +
                    "- LMS Team"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("MAIL ERROR (registration): " + e.getMessage());
        }
    }

    @Override
    public void sendLoginSuccessMail(User user, String ipAddress, LocalDateTime time) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("New Login Detected");
            msg.setText(
                    "Hello " + user.getFirstName() + ",\n\n" +
                    "A successful login was detected.\n\n" +
                    "Time: " + time + "\n" +
                    "IP Address: " + ipAddress + "\n\n" +
                    "If this was not you, please contact admin.\n\n" +
                    "- LMS Security"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("MAIL ERROR (login success): " + e.getMessage());
        }
    }

    @Override
    public void sendLoginFailedMail(String email, String ipAddress, LocalDateTime time) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Failed Login Attempt");
            msg.setText(
                    "Warning,\n\n" +
                    "A failed login attempt was detected.\n\n" +
                    "Time: " + time + "\n" +
                    "IP Address: " + ipAddress + "\n\n" +
                    "If this was not you, please secure your account.\n\n" +
                    "- LMS Security"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("MAIL ERROR (login failed): " + e.getMessage());
        }
    }
}
