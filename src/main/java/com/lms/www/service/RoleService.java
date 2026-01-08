package com.lms.www.service;

import java.util.List;

import com.lms.www.model.Role;

public interface RoleService {

    List<Role> getAllRoles();

    Role createRole(Role role);
}
