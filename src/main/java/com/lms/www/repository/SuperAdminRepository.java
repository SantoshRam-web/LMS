package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.SuperAdmin;

public interface SuperAdminRepository
        extends JpaRepository<SuperAdmin, Long> {

	Optional<SuperAdmin> findByEmail(String email);
	
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
