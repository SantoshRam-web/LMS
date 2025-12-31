package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {}
