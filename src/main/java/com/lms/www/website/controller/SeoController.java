package com.lms.www.website.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.lms.www.website.service.SeoService;

@RestController
@RequestMapping("/website/seo")
public class SeoController {

    private final SeoService seoService;

    public SeoController(SeoService seoService) {
        this.seoService = seoService;
    }

    // SAVE SEO CONFIG
    @PutMapping("/{tenantThemeId}")
    public ResponseEntity<?> saveSeo(
            @PathVariable Long tenantThemeId,
            @RequestBody String seoJson
    ) {
        seoService.saveSeoConfig(tenantThemeId, seoJson);
        return ResponseEntity.ok(Map.of("message", "SEO config saved"));
    }

    // GET SEO CONFIG
    @GetMapping("/{tenantThemeId}")
    public ResponseEntity<?> getSeo(@PathVariable Long tenantThemeId) {
        return ResponseEntity.ok(seoService.getSeoConfig(tenantThemeId));
    }

    // SAVE ROBOTS
    @PutMapping("/{tenantThemeId}/robots")
    public ResponseEntity<?> saveRobots(
            @PathVariable Long tenantThemeId,
            @RequestBody String content
    ) {
        seoService.saveRobots(tenantThemeId, content);
        return ResponseEntity.ok(Map.of("message", "Robots saved"));
    }

    // GET ROBOTS
    @GetMapping("/{tenantThemeId}/robots")
    public ResponseEntity<String> getRobots(@PathVariable Long tenantThemeId) {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(seoService.getRobots(tenantThemeId));
    }

    // UPLOAD SITEMAP
    @PostMapping("/{tenantThemeId}/sitemap")
    public ResponseEntity<?> uploadSitemap(
            @PathVariable Long tenantThemeId,
            @RequestParam("file") MultipartFile file
    ) {
        seoService.uploadSitemap(tenantThemeId, file);
        return ResponseEntity.ok(Map.of("message", "Sitemap uploaded"));
    }

    // GET SITEMAP
    @GetMapping("/{tenantThemeId}/sitemap")
    public ResponseEntity<byte[]> getSitemap(@PathVariable Long tenantThemeId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(seoService.getSitemap(tenantThemeId));
    }

    // DELETE SITEMAP
    @DeleteMapping("/{tenantThemeId}/sitemap")
    public ResponseEntity<?> deleteSitemap(@PathVariable Long tenantThemeId) {
        seoService.deleteSitemap(tenantThemeId);
        return ResponseEntity.ok(Map.of("message", "Sitemap deleted"));
    }
}
