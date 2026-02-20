package com.lms.www.website.service;

import java.util.List;
import java.util.Map;

public interface ThemeService {

    List<Map<String, Object>> getAvailableThemes();

    long applyTheme(Long themeId);

    void publishTheme(Long tenantThemeId);

    Map<String, Object> getLiveThemeStructure();
    
    Object getThemeStructureById(Long tenantThemeId);
    
    void updateSectionConfig(Long sectionId, String configJson);
    
    void resetSection(Long sectionId);
    
    void updatePageTitle(Long pageId, String title);
    
    void deleteDraftTheme(Long tenantThemeId);
    
    void resetEntirePage(Long pageId);
    
    void updateHeaderConfig(Long tenantThemeId, String headerJson);

    String getHeaderConfig(Long tenantThemeId);
    
    void saveFooterConfig(Long tenantThemeId, String configJson);
    String getFooterConfig(Long tenantThemeId);
    
    void saveHeaderConfig(Long tenantThemeId, String headerJson);
}
