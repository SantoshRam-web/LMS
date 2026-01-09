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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(Long userId, String ipAddress) {
        FailedLoginAttempt attempt = new FailedLoginAttempt();
        attempt.setUserId(userId);
        attempt.setAttemptTime(LocalDateTime.now());
        attempt.setIpAddress(ipAddress);
        repository.save(attempt);
    }
}
