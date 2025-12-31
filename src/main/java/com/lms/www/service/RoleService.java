package com.lms.www.service;

import com.lms.www.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role createRole(Role role);
}
