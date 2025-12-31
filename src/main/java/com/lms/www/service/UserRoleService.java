package com.lms.www.service;

import com.lms.www.model.UserRole;

public interface UserRoleService {

    UserRole assignRoleToUser(Long userId, Long roleId);
}
