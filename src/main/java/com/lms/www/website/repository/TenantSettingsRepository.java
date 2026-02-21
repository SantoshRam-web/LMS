package com.lms.www.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.website.model.TenantSettings;

public interface TenantSettingsRepository 
extends JpaRepository<TenantSettings, Long> {
}
