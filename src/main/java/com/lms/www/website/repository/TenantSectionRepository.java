package com.lms.www.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.website.model.TenantSection;

public interface TenantSectionRepository extends JpaRepository<TenantSection, Long> {

    List<TenantSection> findByTenantPage_TenantPageIdOrderByDisplayOrder(Long tenantPageId);
    
    void deleteByTenantPage_TenantPageId(Long tenantPageId);

}
