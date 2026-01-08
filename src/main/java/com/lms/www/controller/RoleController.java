package com.lms.www.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.model.Role;
import com.lms.www.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }
}
