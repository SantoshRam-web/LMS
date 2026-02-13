package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.TenantSection;

public interface TenantSectionRepository extends JpaRepository<TenantSection, Long> {

    List<TenantSection> findByTenantPage_TenantPageIdOrderByDisplayOrder(Long tenantPageId);
}
