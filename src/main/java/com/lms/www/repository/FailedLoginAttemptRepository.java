package com.lms.www.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.FailedLoginAttempt;

public interface FailedLoginAttemptRepository
        extends JpaRepository<FailedLoginAttempt, Long> {

    long countByUserIdAndAttemptTimeAfter(
            Long userId,
            LocalDateTime time
    );

    void deleteByUserId(Long userId);
}
