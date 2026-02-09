package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.UserPermission;

public interface UserPermissionRepository
        extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndPermissionName(
            Long userId,
            String permissionName
    );
}
