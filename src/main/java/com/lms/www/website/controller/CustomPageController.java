package com.lms.www.website.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.*;

import com.lms.www.website.service.CustomPageService;

@RestController
@RequestMapping("/website/custom-page")
@RequiredArgsConstructor
public class CustomPageController {

    private final CustomPageService service;

    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String, String> body) {
        return service.createPage(body.get("title"));
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
}