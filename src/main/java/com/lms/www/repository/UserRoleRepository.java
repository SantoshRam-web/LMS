package com.lms.www.repository;

import com.lms.www.model.User;
import com.lms.www.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    java.util.List<UserRole> findByUser(User user);
}
