package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.UserSession;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.UserSessionService;
import com.lms.www.tenant.TenantContext;
import com.lms.www.config.JwtUtil;

@Service
@Transactional
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final JwtUtil jwtUtil;

    public UserSessionServiceImpl(
            UserSessionRepository userSessionRepository,
            JwtUtil jwtUtil
    ) {
        this.userSessionRepository = userSessionRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void logout(String token) {

        // 1️⃣ Extract tenant DB from JWT
        String tenantDb = jwtUtil.extractTenantDb(token);
        if (tenantDb == null) {
            throw new RuntimeException("Invalid token (tenant missing)");
        }

        // 2️⃣ Set tenant context
        TenantContext.setTenant(tenantDb);

        try {
            // 3️⃣ Tenant DB operation
            UserSession session = userSessionRepository
                    .findByTokenAndLogoutTimeIsNull(token)
                    .orElseThrow(() -> new RuntimeException("Active session not found"));

            session.setLogoutTime(LocalDateTime.now());
            userSessionRepository.save(session);

        } finally {
            // 4️⃣ Always clear
            TenantContext.clear();
        }
    }
}
