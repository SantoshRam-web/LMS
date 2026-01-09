package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.SystemSettings;

public interface SystemSettingsRepository
        extends JpaRepository<SystemSettings, Long> {
	
	Optional<SystemSettings> findByUserId(Long userId);

	boolean existsByUserId(Long userId);

}
