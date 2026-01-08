package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.User;
import com.lms.www.model.UserSession;

public interface UserSessionRepository
        extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByTokenAndLogoutTimeIsNull(String token);
    
    Optional<UserSession> findTopByUserOrderByLoginTimeDesc(User user);

}
