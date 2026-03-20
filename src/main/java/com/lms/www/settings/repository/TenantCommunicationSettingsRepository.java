package com.lms.www.settings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.settings.model.TenantCommunicationSettings;

public interface TenantCommunicationSettingsRepository extends JpaRepository<TenantCommunicationSettings, Long> {
}