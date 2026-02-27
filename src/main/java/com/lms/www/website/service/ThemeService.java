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
    
    void addSection(Long pageId, Long templateSectionId);
    
    void deleteSection(Long sectionId);
    
    Map<String, Object> getPage(Long pageId);

    void unpublishTheme(Long tenantThemeId);
    
    List<Map<String, Object>> getTemplateSections(String pageKey);
    
    void publishPage(Long pageId);

    void unpublishPage(Long pageId);
    
    void reorderSections(Long pageId, List<Long> sectionIds);
}

