package com.lms.www.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.model.Permission;
import com.lms.www.service.PermissionService;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS')")
    public Permission createPermission(
            @RequestBody Permission permission) {

        return permissionService.createPermission(permission);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }
}
