package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.SystemSetting;

@Repository
public interface SettingRepository
        extends JpaRepository<SystemSetting, Long> {
}
