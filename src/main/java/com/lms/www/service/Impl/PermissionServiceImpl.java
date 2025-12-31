package com.lms.www.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.www.model.Permission;
import com.lms.www.repository.PermissionRepository;
import com.lms.www.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
