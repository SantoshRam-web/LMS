package com.lms.www.website.service;

import org.springframework.web.multipart.MultipartFile;
import com.lms.www.website.model.TenantSettings;

public interface SettingsService {

    void updateSiteName(String siteName);

    void uploadLogo(MultipartFile file);

    void uploadFavicon(MultipartFile file);

    void updateFootfall(Boolean enabled);

    void updateStoreTheme(String viewType, String configJson);

    TenantSettings getSettings();
}
