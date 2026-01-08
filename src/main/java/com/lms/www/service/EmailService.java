package com.lms.www.service;

import java.time.LocalDateTime;

import com.lms.www.model.User;

public interface EmailService {

    void sendRegistrationMail(User user, String role);

    void sendLoginSuccessMail(User user, String ipAddress, LocalDateTime time);

    void sendLoginFailedMail(String email, String ipAddress, LocalDateTime time);
}
