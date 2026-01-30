package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.TenantRegistry;

public interface TenantRegistryRepository
extends JpaRepository<TenantRegistry, Long> {

Optional<TenantRegistry> findBySuperAdminUserId(Long userId);
}
