package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.LoginHistory;
import com.lms.www.model.User;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    void deleteByUser(User user);
    
    Optional<LoginHistory> findByUser_UserIdAndIpAddressAndUserAgent(
            Long userId,
            String ipAddress,
            String userAgent
    );

}
