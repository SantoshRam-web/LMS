package com.lms.www.settings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.settings.model.PlatformSettings;

public interface PlatformSettingsRepository extends JpaRepository<PlatformSettings, Long> {
}