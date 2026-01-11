package com.lms.www.service.Impl;

import org.springframework.stereotype.Service;

import com.lms.www.model.Permission;
import com.lms.www.model.RolePermission;
import com.lms.www.repository.PermissionRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public RolePermissionServiceImpl(
            RolePermissionRepository rolePermissionRepository,
            PermissionRepository permissionRepository,
            UserRepository userRepository
    ) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RolePermission assignPermissionToUser(
            Long userId,
            Long permissionId
    ) {

        // ✅ Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Validate permission exists
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        // ✅ Correct mapping (ID-based)
        RolePermission rp = new RolePermission();
        rp.setUserId(userId);          // ✅ THIS IS CORRECT
        rp.setPermission(permission);  // ✅ THIS IS CORRECT

        return rolePermissionRepository.save(rp);
    }
}
