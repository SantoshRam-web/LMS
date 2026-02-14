package com.lms.www.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.website.model.TenantPage;

public interface TenantPageRepository extends JpaRepository<TenantPage, Long> {

    List<TenantPage> findByTenantTheme_TenantThemeId(Long tenantThemeId);
}
