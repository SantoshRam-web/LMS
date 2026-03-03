package com.lms.www.website.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.website.service.CustomPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/website/custom-page")
@RequiredArgsConstructor
public class CustomPageController {

    private final CustomPageService service;

    @PostMapping
    public ResponseEntity<?> createPage(@RequestBody Map<String, String> body) {

        String title = body.get("title");
        String slug = body.get("slug");

        return ResponseEntity.ok(service.createPage(title, slug));
    }

    @PostMapping("/{id}/copy")
    public Map<String, Object> copy(@PathVariable Long id) {
        return service.copyPage(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePage(id);
    }

    @PostMapping("/{id}/publish")
    public void publish(@PathVariable Long id) {
        service.publishPage(id);
    }

    @PostMapping("/{id}/unpublish")
    public void unpublish(@PathVariable Long id) {
        service.unpublishPage(id);
    }

    @PutMapping("/{id}/metadata")
    public void metadata(@PathVariable Long id,
                         @RequestBody Map<String, String> body) {
        service.updateMetadata(id,
                body.get("metaTitle"),
                body.get("metaDescription"));
    }

    @GetMapping("/search")
    public List<?> search(@RequestParam String q) {
        return service.searchPages(q);
    }

    @PostMapping("/{id}/reset")
    public void reset(@PathVariable Long id) {
        service.resetPage(id);
    }

    @PostMapping("/{id}/sections")
    public void addSection(@PathVariable Long id,
                           @RequestBody Map<String, String> body) {
        service.addSection(id,
                body.get("sectionType"),
                body.get("config"));
    }

    @PutMapping("/sections/{sectionId}")
    public void updateSection(@PathVariable Long sectionId,
                              @RequestBody Map<String, String> body) {
        service.updateSection(sectionId,
                body.get("config"));
    }

    @DeleteMapping("/sections/{sectionId}")
    public void deleteSection(@PathVariable Long sectionId) {
        service.deleteSection(sectionId);
    }

    @PutMapping("/{id}/sections/order")
    public void reorder(@PathVariable Long id,
                        @RequestBody List<Long> orderedIds) {
        service.reorderSections(id, orderedIds);
    }
    
    @GetMapping("/{pageId}/builder")
    public ResponseEntity<?> getPageBuilder(@PathVariable Long pageId) {
        return ResponseEntity.ok(service.getPageBuilder(pageId));
    }
    
    @PutMapping("/{pageId}/save")
    public ResponseEntity<?> savePage(@PathVariable Long pageId) {
        service.savePage(pageId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{pageId}/preview")
    public ResponseEntity<?> previewPage(@PathVariable Long pageId) {
        return ResponseEntity.ok(service.previewPage(pageId));
    }
}