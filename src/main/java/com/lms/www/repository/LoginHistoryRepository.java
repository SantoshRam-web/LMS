package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.LoginHistory;

@Repository
public interface LoginHistoryRepository
        extends JpaRepository<LoginHistory, Long> {
}
