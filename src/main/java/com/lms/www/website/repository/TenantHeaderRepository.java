package com.lms.www.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.website.model.TenantHeader;

public interface TenantHeaderRepository 
        extends JpaRepository<TenantHeader, Long> {

	List<TenantHeader> findAllByOrderByCreatedAtDesc();
}