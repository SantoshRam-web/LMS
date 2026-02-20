package com.lms.www.website.service;

import org.springframework.web.multipart.MultipartFile;

public interface SeoService {

    void saveSeoConfig(Long tenantThemeId, String seoJson);

    String getSeoConfig(Long tenantThemeId);

    void saveRobots(Long tenantThemeId, String robotsContent);

    String getRobots(Long tenantThemeId);

    void uploadSitemap(Long tenantThemeId, MultipartFile file);

    byte[] getSitemap(Long tenantThemeId);

    void deleteSitemap(Long tenantThemeId);
}
