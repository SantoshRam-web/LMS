package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.TenantPage;

public interface TenantPageRepository extends JpaRepository<TenantPage, Long> {

    List<TenantPage> findByTenantTheme_TenantThemeId(Long tenantThemeId);
}
