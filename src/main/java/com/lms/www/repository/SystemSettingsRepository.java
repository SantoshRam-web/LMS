package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.model.SystemSettings;

public interface SystemSettingsRepository
        extends JpaRepository<SystemSettings, Long> {
}
