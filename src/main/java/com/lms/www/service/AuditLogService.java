package com.lms.www.service;

import com.lms.www.model.User;

public interface AuditLogService {

    void log(
        String action,
        String entityName,
        Long entityId,
        User user,
        String ipAddress
    );
}
