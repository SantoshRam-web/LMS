package com.lms.www.website.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.website.model.TenantPage;
import com.lms.www.website.model.TenantSection;
import com.lms.www.website.model.TenantTheme;
import com.lms.www.website.repository.TenantPageRepository;
import com.lms.www.website.repository.TenantSectionRepository;
import com.lms.www.website.repository.TenantThemeRepository;
import com.lms.www.website.service.ThemeService;

@Service
public class ThemeServiceImpl implements ThemeService {

    private final JdbcTemplate jdbcTemplate;
    private final TenantThemeRepository tenantThemeRepository;
    private final TenantPageRepository tenantPageRepository;
    private final TenantSectionRepository tenantSectionRepository;

    public ThemeServiceImpl(
            JdbcTemplate jdbcTemplate,
            TenantThemeRepository tenantThemeRepository,
            TenantPageRepository tenantPageRepository,
            TenantSectionRepository tenantSectionRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.tenantThemeRepository = tenantThemeRepository;
        this.tenantPageRepository = tenantPageRepository;
        this.tenantSectionRepository = tenantSectionRepository;
    }

    // =========================================
    // 1️⃣ List available themes (MASTER DB)
    // =========================================
    @Override
    public List<Map<String, Object>> getAvailableThemes() {
        return jdbcTemplate.queryForList(
                "SELECT theme_id, name, description, preview_image_url, version " +
                "FROM theme_templates"
        );
    }

    // =========================================
    // 2️⃣ Apply Theme (Clone MASTER → TENANT)
    // =========================================
    @Override
    @Transactional
    public void applyTheme(Long themeId) {

        TenantTheme tenantTheme = new TenantTheme();
        tenantTheme.setThemeTemplateId(themeId);
        tenantTheme.setStatus("DRAFT");
        tenantTheme = tenantThemeRepository.save(tenantTheme);

        // Fetch pages from MASTER
        List<Map<String, Object>> pages =
                jdbcTemplate.queryForList(
                        "SELECT template_page_id, page_key FROM theme_template_pages WHERE theme_id = ?",
                        themeId
                );

        for (Map<String, Object> pageRow : pages) {

            Long templatePageId =
                    ((Number) pageRow.get("template_page_id")).longValue();

            TenantPage tenantPage = new TenantPage();
            tenantPage.setTenantTheme(tenantTheme);
            tenantPage.setPageKey((String) pageRow.get("page_key"));
            tenantPage.setCustomTitle((String) pageRow.get("page_key"));
            tenantPage.setIsPublished(false);
            tenantPage = tenantPageRepository.save(tenantPage);

            // Fetch sections from MASTER
            List<Map<String, Object>> sections =
                    jdbcTemplate.queryForList(
                            "SELECT template_section_id, section_type, default_config, display_order\r\n"
                            + "FROM theme_template_sections WHERE template_page_id = ?",
                            templatePageId
                    );

            for (Map<String, Object> sectionRow : sections) {

            	Long templateSectionId =
            	        ((Number) sectionRow.get("template_section_id")).longValue();

            	TenantSection section = new TenantSection();
            	section.setTenantPage(tenantPage);
            	section.setTemplateSectionId(templateSectionId);   // ✅ IMPORTANT
            	section.setSectionType((String) sectionRow.get("section_type"));
            	section.setSectionConfig(
            	        sectionRow.get("default_config") != null
            	                ? sectionRow.get("default_config").toString()
            	                : "{}"
            	);
            	section.setDisplayOrder(
            	        sectionRow.get("display_order") != null
            	                ? ((Number) sectionRow.get("display_order")).intValue()
            	                : 0
            	);

            	tenantSectionRepository.save(section);
            }
        }
    }

    // =========================================
    // 3️⃣ Publish Theme
    // =========================================
    @Override
    @Transactional
    public void publishTheme(Long tenantThemeId) {

        TenantTheme themeToPublish = tenantThemeRepository
                .findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        // Only one LIVE theme per tenant
        tenantThemeRepository.findByStatus("LIVE")
                .ifPresent(existingLive -> {
                    existingLive.setStatus("DRAFT");
                    tenantThemeRepository.save(existingLive);
                });

        themeToPublish.setStatus("LIVE");
        tenantThemeRepository.save(themeToPublish);
    }

    // =========================================
    // 4️⃣ Get LIVE Theme Structure (Public)
    // =========================================
    @Override
    public Map<String, Object> getLiveThemeStructure() {

        return tenantThemeRepository.findByStatus("LIVE")
                .map(live -> Map.of(
                        "tenantThemeId", live.getTenantThemeId(),
                        "pages",
                        tenantPageRepository
                                .findByTenantTheme_TenantThemeId(live.getTenantThemeId())
                                .stream()
                                .map(page -> Map.of(
                                        "pageKey", page.getPageKey(),
                                        "title", page.getCustomTitle(),
                                        "sections",
                                        tenantSectionRepository
                                                .findByTenantPage_TenantPageIdOrderByDisplayOrder(
                                                        page.getTenantPageId()
                                                )
                                ))
                                .toList()
                ))
                .orElse(Map.of("message", "Default theme"));
    }

    // =========================================
    // 5️⃣ Preview Theme Structure by ID
    // =========================================
    @Override
    public Object getThemeStructureById(Long tenantThemeId) {

        TenantTheme tenantTheme = tenantThemeRepository.findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        List<TenantPage> pages =
                tenantPageRepository.findByTenantTheme_TenantThemeId(tenantThemeId);

        return pages.stream().map(page -> {
            List<TenantSection> sections =
                    tenantSectionRepository
                            .findByTenantPage_TenantPageIdOrderByDisplayOrder(
                                    page.getTenantPageId()
                            );

            return Map.of(
                    "pageKey", page.getPageKey(),
                    "title", page.getCustomTitle(),
                    "sections", sections
            );
        }).toList();
    }

    // =========================================
    // 6️⃣ Update Section Config
    // =========================================
    @Override
    @Transactional
    public void updateSectionConfig(Long sectionId, String configJson) {

        TenantSection section = tenantSectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        section.setSectionConfig(configJson);
        tenantSectionRepository.save(section);
    }

    // =========================================
    // 7️⃣ Reset Section (Fetch from MASTER DB)
    // =========================================
    @Override
    @Transactional
    public void resetSection(Long sectionId) {

        TenantSection section = tenantSectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        String pageKey = section.getTenantPage().getPageKey();
        Long themeTemplateId = section.getTenantPage()
                .getTenantTheme()
                .getThemeTemplateId();

        // Fetch template_page_id from MASTER
        Long templatePageId = jdbcTemplate.queryForObject(
                "SELECT template_page_id FROM theme_template_pages " +
                "WHERE theme_id = ? AND page_key = ?",
                Long.class,
                themeTemplateId,
                pageKey
        );

        List<String> configs = jdbcTemplate.query(
                "SELECT default_config FROM theme_template_sections " +
                "WHERE template_page_id = ? AND section_type = ? " +
                "ORDER BY template_section_id ASC",
                (rs, rowNum) -> rs.getString("default_config"),
                templatePageId,
                section.getSectionType()
        );

        if (configs.isEmpty()) {
            throw new RuntimeException("Template section not found");
        }

        String defaultConfig = configs.get(0);

        section.setSectionConfig(defaultConfig != null ? defaultConfig : "{}");
        tenantSectionRepository.save(section);
    }

    // =========================================
    // 8️⃣ Update Page Title
    // =========================================
    @Override
    @Transactional
    public void updatePageTitle(Long pageId, String title) {

        TenantPage page = tenantPageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        page.setCustomTitle(title);
        tenantPageRepository.save(page);
    }
}
