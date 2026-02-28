package com.lms.www.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.website.model.TenantCustomPageSection;

import java.util.List;

public interface TenantCustomPageSectionRepository
        extends JpaRepository<TenantCustomPageSection, Long> {

    List<TenantCustomPageSection>
    findByTenantCustomPage_TenantCustomPageIdOrderByDisplayOrderAsc(Long pageId);

    void deleteByTenantCustomPage_TenantCustomPageId(Long pageId);
}