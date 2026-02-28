package com.lms.www.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.website.model.TenantCustomPage;

import java.util.Optional;
import java.util.List;

public interface TenantCustomPageRepository
        extends JpaRepository<TenantCustomPage, Long> {

    Optional<TenantCustomPage> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<TenantCustomPage> findByTitleContainingIgnoreCase(String title);
}