package com.lms.www.settings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.settings.model.TenantSecuritySettings;

public interface TenantSecuritySettingsRepository extends JpaRepository<TenantSecuritySettings, Long> {
}