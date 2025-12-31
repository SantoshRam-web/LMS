package com.lms.www.service.Impl;

import org.springframework.stereotype.Service;

import com.lms.www.model.Permission;
import com.lms.www.model.Role;
import com.lms.www.model.RolePermission;
import com.lms.www.repository.PermissionRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.RoleRepository;
import com.lms.www.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionServiceImpl(
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository,
            PermissionRepository permissionRepository) {

        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RolePermission assignPermissionToRole(Long roleId, Long permissionId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        RolePermission rp = new RolePermission();
        rp.setRole(role);
        rp.setPermission(permission);

        return rolePermissionRepository.save(rp);
    }
}
