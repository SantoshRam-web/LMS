package com.lms.www.service;

public interface FailedLoginAttemptService {
    void recordFailure(Long userId, String ipAddress);
}
