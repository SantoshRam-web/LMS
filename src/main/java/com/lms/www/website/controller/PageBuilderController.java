package com.lms.www.website.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.www.website.service.ThemeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/website")
public class PageBuilderController {

    private final ThemeService themeService;

    public PageBuilderController(ThemeService themeService) {
        this.themeService = themeService;
    }

    // 🔹 Load page for edit
    @GetMapping("/page/{pageId}")
    public ResponseEntity<?> getPage(@PathVariable Long pageId) {
        return ResponseEntity.ok(themeService.getPage(pageId));
    }

    // 🔹 Fetch template sections
    @GetMapping("/templates/sections")
    public ResponseEntity<?> getTemplateSections(
            @RequestParam String pageKey) {

        return ResponseEntity.ok(
                themeService.getTemplateSections(pageKey)
        );
    }

    // 🔹 Add section
    @PostMapping("/page/{pageId}/sections")
    public ResponseEntity<?> addSection(
            @PathVariable Long pageId,
            @RequestBody Map<String, Long> body) {

        themeService.addSection(pageId, body.get("templateSectionId"));
        return ResponseEntity.ok("Section added");
    }

    // 🔹 Delete section
    @DeleteMapping("/page/section/{sectionId}")
    public ResponseEntity<?> deleteSection(
            @PathVariable Long sectionId) {

        themeService.deleteSection(sectionId);
        return ResponseEntity.ok("Section deleted");
    }

    // 🔹 Reorder sections
    @PutMapping("/page/{pageId}/sections/order")
    public ResponseEntity<?> reorderSections(
            @PathVariable Long pageId,
            @RequestBody List<Long> sectionIds) {

        themeService.reorderSections(pageId, sectionIds);
        return ResponseEntity.ok("Sections reordered");
    }

    // 🔹 Unpublish theme
    @PostMapping("/theme/{themeId}/unpublish")
    public ResponseEntity<?> unpublishTheme(
            @PathVariable Long themeId) {

        themeService.unpublishTheme(themeId);
        return ResponseEntity.ok("Theme unpublished");
    }
    
    @PostMapping("/page/{pageId}/publish")
    public ResponseEntity<?> publishPage(@PathVariable Long pageId) {

        themeService.publishPage(pageId);
        return ResponseEntity.ok("Page published");
    }
    
    @PostMapping("/page/{pageId}/unpublish")
    public ResponseEntity<?> unpublishPage(@PathVariable Long pageId) {

        themeService.unpublishPage(pageId);
        return ResponseEntity.ok("Page unpublished");
    }
}