package com.lms.www.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.www.model.Role;
import com.lms.www.repository.RoleRepository;
import com.lms.www.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }
}
