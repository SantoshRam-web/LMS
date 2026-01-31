package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.model.SuperAdmin;

public interface SuperAdminRepository
        extends JpaRepository<SuperAdmin, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
