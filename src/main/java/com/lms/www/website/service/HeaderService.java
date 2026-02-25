package com.lms.www.website.service;

import java.util.List;

import com.lms.www.website.model.TenantHeader;

public interface HeaderService {

    Long saveHeader(String headerConfig);

    List<TenantHeader> getHeaders();

    void applyHeaderToTheme(Long tenantThemeId, Long headerId);

    void revertToDefault(Long tenantThemeId);
}