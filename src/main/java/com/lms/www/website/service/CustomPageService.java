package com.lms.www.website.service;

import java.util.Map;
import java.util.List;

public interface CustomPageService {

    Map<String, Object> createPage(String title);

    Map<String, Object> copyPage(Long pageId);

    void deletePage(Long pageId);

    void publishPage(Long pageId);

    void unpublishPage(Long pageId);

    void updateMetadata(Long pageId, String metaTitle, String metaDescription);

    List<?> searchPages(String keyword);

    void resetPage(Long pageId);

    void addSection(Long pageId, String sectionType, String config);

    void updateSection(Long sectionId, String config);

    void deleteSection(Long sectionId);

    void reorderSections(Long pageId, List<Long> orderedSectionIds);
}