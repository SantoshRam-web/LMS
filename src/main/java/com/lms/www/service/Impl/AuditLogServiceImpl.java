package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.lms.www.model.AuditLog;
import com.lms.www.model.User;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.service.AuditLogService;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;

    }
    
   
    @Override
    public void log(
            String action,
            String entityName,
            Long entityId,
            User user,
            String ipAddress) {

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setUserId(user.getUserId());
        log.setCreatedTime(LocalDateTime.now());
        log.setIpAddress(ipAddress);

        auditLogRepository.save(log);
    }
}
