package com.lms.www.website.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.website.model.TenantCustomPage;
import com.lms.www.website.model.TenantCustomPageSection;
import com.lms.www.website.repository.TenantCustomPageRepository;
import com.lms.www.website.repository.TenantCustomPageSectionRepository;
import com.lms.www.website.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/website/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final TenantCustomPageRepository pageRepo;
    private final TenantCustomPageSectionRepository sectionRepo;
   
    @GetMapping
    public ResponseEntity<?> getStoreData() {
        return ResponseEntity.ok(storeService.getStoreData());
    }
    
    @GetMapping("/s/pages/{slug}")
    public Object renderCustomPage(@PathVariable String slug) {

        TenantCustomPage page =
                pageRepo.findBySlug(slug)
                        .orElseThrow(() -> new RuntimeException("Page not found"));

        if (!"PUBLISHED".equals(page.getStatus())) {
            throw new RuntimeException("Page not published");
        }

        List<TenantCustomPageSection> sections =
                sectionRepo
                    .findByTenantCustomPage_TenantCustomPageIdOrderByDisplayOrderAsc(
                            page.getTenantCustomPageId()
                    );

        return Map.of(
                "page", page,
                "sections", sections
        );
    }
}