package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {}