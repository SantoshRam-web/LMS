package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.FailedLoginAttempt;

public interface FailedLoginAttemptRepository
        extends JpaRepository<FailedLoginAttempt, Long> {
}
