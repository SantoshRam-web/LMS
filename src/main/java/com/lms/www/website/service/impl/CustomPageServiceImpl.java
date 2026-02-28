package com.lms.www.website.service.impl;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.lms.www.website.model.*;
import com.lms.www.website.repository.*;
import com.lms.www.website.service.CustomPageService;

import java.util.*;
import java.text.Normalizer;

@Service
@RequiredArgsConstructor
public class CustomPageServiceImpl implements CustomPageService {

    private final TenantCustomPageRepository pageRepo;
    private final TenantCustomPageSectionRepository sectionRepo;

    // ---------- CREATE PAGE ----------
    @Override
    public Map<String, Object> createPage(String title) {
        String slug = generateUniqueSlug(title);

        TenantCustomPage page = new TenantCustomPage();
        page.setTitle(title);
        page.setSlug(slug);
        page.setStatus("DRAFT");

        pageRepo.save(page);

        return Map.of("pageId", page.getTenantCustomPageId(), "slug", slug);
    }

    // ---------- COPY PAGE ----------
    @Override
    public Map<String, Object> copyPage(Long pageId) {

        TenantCustomPage original = pageRepo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        String newTitle = original.getTitle() + " - Copy";
        String newSlug = generateUniqueSlug(newTitle);

        TenantCustomPage copy = new TenantCustomPage();
        copy.setTitle(newTitle);
        copy.setSlug(newSlug);
        copy.setStatus("DRAFT");
        copy.setMetaTitle(original.getMetaTitle());
        copy.setMetaDescription(original.getMetaDescription());

        pageRepo.save(copy);

        List<TenantCustomPageSection> sections =
                sectionRepo.findByTenantCustomPage_TenantCustomPageIdOrderByDisplayOrderAsc(pageId);

        for (TenantCustomPageSection sec : sections) {
            TenantCustomPageSection newSec = new TenantCustomPageSection();
            newSec.setTenantCustomPage(copy);
            newSec.setSectionType(sec.getSectionType());
            newSec.setSectionConfig(sec.getSectionConfig());
            newSec.setDisplayOrder(sec.getDisplayOrder());
            sectionRepo.save(newSec);
        }

        return Map.of("pageId", copy.getTenantCustomPageId());
    }

    // ---------- PUBLISH ----------
    @Override
    public void publishPage(Long pageId) {
        TenantCustomPage page = pageRepo.findById(pageId).orElseThrow();
        page.setStatus("PUBLISHED");
        pageRepo.save(page);
    }

    @Override
    public void unpublishPage(Long pageId) {
        TenantCustomPage page = pageRepo.findById(pageId).orElseThrow();
        page.setStatus("DRAFT");
        pageRepo.save(page);
    }

    // ---------- DELETE ----------
    @Override
    public void deletePage(Long pageId) {
        pageRepo.deleteById(pageId);
    }

    // ---------- METADATA ----------
    @Override
    public void updateMetadata(Long pageId, String metaTitle, String metaDescription) {
        TenantCustomPage page = pageRepo.findById(pageId).orElseThrow();
        page.setMetaTitle(metaTitle);
        page.setMetaDescription(metaDescription);
        pageRepo.save(page);
    }

    // ---------- SEARCH ----------
    @Override
    public List<?> searchPages(String keyword) {
        return pageRepo.findByTitleContainingIgnoreCase(keyword);
    }

    // ---------- RESET ----------
    @Override
    public void resetPage(Long pageId) {
        sectionRepo.deleteByTenantCustomPage_TenantCustomPageId(pageId);
    }

    // ---------- ADD SECTION ----------
    @Override
    public void addSection(Long pageId, String sectionType, String config) {

        TenantCustomPage page = pageRepo.findById(pageId).orElseThrow();

        int order = sectionRepo
                .findByTenantCustomPage_TenantCustomPageIdOrderByDisplayOrderAsc(pageId)
                .size() + 1;

        TenantCustomPageSection sec = new TenantCustomPageSection();
        sec.setTenantCustomPage(page);
        sec.setSectionType(sectionType);
        sec.setSectionConfig(config);
        sec.setDisplayOrder(order);

        sectionRepo.save(sec);
    }

    // ---------- UPDATE SECTION ----------
    @Override
    public void updateSection(Long sectionId, String config) {
        TenantCustomPageSection sec = sectionRepo.findById(sectionId).orElseThrow();
        sec.setSectionConfig(config);
        sectionRepo.save(sec);
    }

    // ---------- DELETE SECTION ----------
    @Override
    public void deleteSection(Long sectionId) {
        sectionRepo.deleteById(sectionId);
    }

    // ---------- REORDER ----------
    @Override
    public void reorderSections(Long pageId, List<Long> orderedIds) {

        int order = 1;
        for (Long id : orderedIds) {
            TenantCustomPageSection sec = sectionRepo.findById(id).orElseThrow();
            sec.setDisplayOrder(order++);
            sectionRepo.save(sec);
        }
    }

    // ---------- SLUG GENERATOR ----------
    private String generateUniqueSlug(String title) {

        String baseSlug = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("[^\\w\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .toLowerCase();

        String slug = baseSlug;
        int counter = 1;

        while (pageRepo.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }
}