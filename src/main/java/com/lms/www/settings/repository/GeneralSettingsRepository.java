package com.lms.www.settings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.settings.model.GeneralSettings;

public interface GeneralSettingsRepository extends JpaRepository<GeneralSettings, Long> {
}