package com.lms.www.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AccountUnlockService {
    void requestUnlock(String email, HttpServletRequest request);
}

