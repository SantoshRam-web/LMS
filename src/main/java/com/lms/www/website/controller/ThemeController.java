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
    public ResponseEntity<Map<String, Object>> applyTheme(
            @PathVariable Long themeId,
            HttpServletRequest request
    ) {
        enforceSuperAdmin(request);

        Long newTenantThemeId = themeService.applyTheme(themeId);

        return ResponseEntity.ok(Map.of(
                "message", "Theme applied as DRAFT",
                "tenantThemeId", newTenantThemeId
        ));
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
    
    @DeleteMapping("/{tenantThemeId}")
    public ResponseEntity<?> deleteDraftTheme(
            @PathVariable Long tenantThemeId) {

        themeService.deleteDraftTheme(tenantThemeId);

        return ResponseEntity.ok(
                Map.of("message", "Draft theme deleted successfully")
        );
    }
    
    @PutMapping("/page/{pageId}/reset")
    public ResponseEntity<?> resetEntirePage(@PathVariable Long pageId) {

        themeService.resetEntirePage(pageId);

        return ResponseEntity.ok(
                Map.of("message", "Page reset to template default")
        );
    }
    
    @PutMapping("/{tenantThemeId}/header")
    public ResponseEntity<?> updateHeader(
            @PathVariable Long tenantThemeId,
            @RequestBody String headerJson
    ) {
        themeService.updateHeaderConfig(tenantThemeId, headerJson);
        return ResponseEntity.ok(Map.of("message", "Header updated"));
    }

    @GetMapping("/{tenantThemeId}/header")
    public ResponseEntity<?> getHeader(
            @PathVariable Long tenantThemeId
    ) {
        String header = themeService.getHeaderConfig(tenantThemeId);
        return ResponseEntity.ok(header != null ? header : "{}");
    }
    
    @PutMapping("/{tenantThemeId}/footer")
    public ResponseEntity<?> saveFooter(
            @PathVariable Long tenantThemeId,
            @RequestBody String configJson
    ) {
        themeService.saveFooterConfig(tenantThemeId, configJson);
        return ResponseEntity.ok(Map.of("message", "Footer saved"));
    }
    
    @GetMapping("/{tenantThemeId}/footer")
    public ResponseEntity<?> getFooter(
            @PathVariable Long tenantThemeId
    ) {
        return ResponseEntity.ok(
                themeService.getFooterConfig(tenantThemeId)
        );
    }
    
    @PutMapping("/header/{tenantThemeId}")
    public ResponseEntity<?> saveHeader(
            @PathVariable Long tenantThemeId,
            @RequestBody String headerJson
    ) {
        themeService.saveHeaderConfig(tenantThemeId, headerJson);

        return ResponseEntity.ok(
                Map.of("message", "Header configuration saved")
        );
    }
    
    @RestController
    @RequestMapping("/website/page")
    public class PageBuilderController {

        private final ThemeService themeService;

        public PageBuilderController(ThemeService themeService) {
            this.themeService = themeService;
        }

        @PostMapping("/{pageId}/sections")
        public ResponseEntity<?> addSection(
                @PathVariable Long pageId,
                @RequestBody Map<String, Long> body) {

            themeService.addSection(pageId, body.get("templateSectionId"));
            return ResponseEntity.ok("Section added");
        }

        @DeleteMapping("/section/{sectionId}")
        public ResponseEntity<?> deleteSection(
                @PathVariable Long sectionId) {

            themeService.deleteSection(sectionId);
            return ResponseEntity.ok("Section deleted");
        }

        @PutMapping("/{pageId}/sections/order")
        public ResponseEntity<?> reorderSections(
                @PathVariable Long pageId,
                @RequestBody List<Long> sectionIds) {

            themeService.reorderSections(pageId, sectionIds);
            return ResponseEntity.ok("Sections reordered");
        }
    }
}
