package com.lms.www.service;

import java.util.List;
import com.lms.www.model.Permission;

public interface PermissionService {

    Permission createPermission(Permission permission);

    List<Permission> getAllPermissions();
}
