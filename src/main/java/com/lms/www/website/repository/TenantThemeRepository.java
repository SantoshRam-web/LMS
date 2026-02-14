package com.lms.www.website.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.website.model.TenantTheme;

public interface TenantThemeRepository extends JpaRepository<TenantTheme, Long> {

    Optional<TenantTheme> findByStatus(String status);
}
