package com.lms.www.website.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.www.model.User;
import com.lms.www.website.service.ThemeService;

@RestController
@RequestMapping("/website/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    // =========================================
    // 🔓 PUBLIC – Get LIVE theme (no login required)
    // =========================================
    @GetMapping("/live")
    public ResponseEntity<?> getLiveTheme() {
        return ResponseEntity.ok(themeService.getLiveThemeStructure());
    }

    // =========================================
    // 🔐 SUPER ADMIN – List available templates
    // =========================================
    @GetMapping("/templates")
    public ResponseEntity<List<Map<String, Object>>> getAvailableThemes(
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);
        return ResponseEntity.ok(themeService.getAvailableThemes());
    }

    // =========================================
    // 🔐 SUPER ADMIN – Apply theme (DRAFT)
    // =========================================
    @PostMapping("/apply/{themeId}")
    public ResponseEntity<String> applyTheme(
            @PathVariable Long themeId,
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);
        themeService.applyTheme(themeId);
        return ResponseEntity.ok("Theme applied as DRAFT");
    }

    // =========================================
    // 🔐 SUPER ADMIN – Publish theme
    // =========================================
    @PostMapping("/publish/{tenantThemeId}")
    public ResponseEntity<String> publishTheme(
            @PathVariable Long tenantThemeId,
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);
        themeService.publishTheme(tenantThemeId);
        return ResponseEntity.ok("Theme published successfully");
    }

    // =========================================
    // 🔐 SUPER ADMIN – Preview specific theme
    // =========================================
    @GetMapping("/{tenantThemeId}")
    public ResponseEntity<?> previewTheme(
            @PathVariable Long tenantThemeId,
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);
        return ResponseEntity.ok(
                themeService.getThemeStructureById(tenantThemeId)
        );
    }

    // =========================================
    // 🔐 SUPER ADMIN – Update section
    // =========================================
    @PutMapping("/section/{sectionId}")
    public ResponseEntity<String> updateSection(
            @PathVariable Long sectionId,
            @RequestBody String configJson,
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);
        themeService.updateSectionConfig(sectionId, configJson);
        return ResponseEntity.ok("Section updated");
    }

    // =========================================
    // 🔐 SUPER ADMIN – Reset section
    // =========================================
    @PostMapping("/section/{sectionId}/reset")
    public ResponseEntity<String> resetSection(
            @PathVariable Long sectionId,
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);
        themeService.resetSection(sectionId);
        return ResponseEntity.ok("Section reset to default");
    }

    // =========================================
    // 🔐 SUPER ADMIN – Update page title
    // =========================================
    @PutMapping("/page/{pageId}/title")
    public ResponseEntity<String> updatePageTitle(
            @PathVariable Long pageId,
            @RequestBody String title,
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);
        themeService.updatePageTitle(pageId, title);
        return ResponseEntity.ok("Page title updated");
    }

    // =========================================
    // 🔒 SUPER ADMIN CHECK
    // =========================================
    private void enforceSuperAdmin(HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");

        if (user == null || !"ROLE_SUPER_ADMIN".equals(user.getRoleName())) {
            throw new RuntimeException("Access denied. Super Admin only.");
        }
    }
}
