package com.lms.www.service;

import com.lms.www.model.RolePermission;

public interface RolePermissionService {

    RolePermission assignPermissionToRole(Long roleId, Long permissionId);
}
