package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.UserSession;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.UserSessionService;

@Service
@Transactional
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSessionServiceImpl(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public void logout(String token) {

        UserSession session = userSessionRepository
                .findByTokenAndLogoutTimeIsNull(token)
                .orElseThrow(() -> new RuntimeException("Active session not found"));

        session.setLogoutTime(LocalDateTime.now());
        userSessionRepository.save(session);
    }
}
