package com.lms.www.service;

import com.lms.www.model.User;

import java.time.LocalDateTime;

public interface EmailService {

    void sendRegistrationMail(User user, String role);

    void sendLoginSuccessMail(User user, String ipAddress, LocalDateTime time);

    void sendLoginFailedMail(String email, String ipAddress, LocalDateTime time);
}
