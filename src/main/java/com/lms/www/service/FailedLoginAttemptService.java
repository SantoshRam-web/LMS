package com.lms.www.service;

public interface FailedLoginAttemptService {

    void recordFailedAttempt(Long userId, String ipAddress);

    long countRecentAttempts(Long userId, long minutes);

    void clearAttempts(Long userId);
}
