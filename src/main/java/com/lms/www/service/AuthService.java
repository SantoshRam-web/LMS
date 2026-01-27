package com.lms.www.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    String login(String email, String password, String ipAddress, HttpServletRequest request);
    
}
