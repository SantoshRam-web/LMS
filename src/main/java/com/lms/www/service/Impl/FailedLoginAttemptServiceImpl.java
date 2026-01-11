package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.FailedLoginAttempt;
import com.lms.www.repository.FailedLoginAttemptRepository;
import com.lms.www.service.FailedLoginAttemptService;

@Service
public class FailedLoginAttemptServiceImpl implements FailedLoginAttemptService {

    private final FailedLoginAttemptRepository repository;

    public FailedLoginAttemptServiceImpl(FailedLoginAttemptRepository repository) {
        this.repository = repository;
    }

    /**
     * MUST commit even if login fails
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedAttempt(Long userId, String ipAddress) {
        FailedLoginAttempt attempt = new FailedLoginAttempt();
        attempt.setUserId(userId);
        attempt.setAttemptTime(LocalDateTime.now());
        attempt.setIpAddress(ipAddress);
        repository.save(attempt);
    }

    @Override
    public long countRecentAttempts(Long userId, long minutes) {
        LocalDateTime window = LocalDateTime.now().minusMinutes(minutes);
        return repository.countByUserIdAndAttemptTimeAfter(userId, window);
    }

    @Override
    public void clearAttempts(Long userId) {
        repository.deleteByUserId(userId);
    }
}
