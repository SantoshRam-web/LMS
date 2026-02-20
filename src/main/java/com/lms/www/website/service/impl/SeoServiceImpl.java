package com.lms.www.website.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lms.www.website.model.TenantTheme;
import com.lms.www.website.repository.TenantThemeRepository;
import com.lms.www.website.service.SeoService;

@Service
public class SeoServiceImpl implements SeoService {

    private final TenantThemeRepository tenantThemeRepository;

    public SeoServiceImpl(TenantThemeRepository tenantThemeRepository) {
        this.tenantThemeRepository = tenantThemeRepository;
    }

    // 1️⃣ SAVE SEO CONFIG
    @Override
    @Transactional
    public void saveSeoConfig(Long tenantThemeId, String seoJson) {

        TenantTheme theme = tenantThemeRepository.findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        theme.setSeoConfig(seoJson);
        tenantThemeRepository.save(theme);
    }

    // 2️⃣ GET SEO CONFIG
    @Override
    public String getSeoConfig(Long tenantThemeId) {

        TenantTheme theme = tenantThemeRepository.findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        return theme.getSeoConfig();
    }

    // 3️⃣ SAVE ROBOTS
    @Override
    @Transactional
    public void saveRobots(Long tenantThemeId, String robotsContent) {

        TenantTheme theme = tenantThemeRepository.findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        theme.setRobotsTxt(robotsContent);
        tenantThemeRepository.save(theme);
    }

    // 4️⃣ GET ROBOTS
    @Override
    public String getRobots(Long tenantThemeId) {

        TenantTheme theme = tenantThemeRepository.findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        return theme.getRobotsTxt();
    }

    // 5️⃣ UPLOAD SITEMAP
    @Override
    @Transactional
    public void uploadSitemap(Long tenantThemeId, MultipartFile file) {

        try {
            TenantTheme theme = tenantThemeRepository.findById(tenantThemeId)
                    .orElseThrow(() -> new RuntimeException("Theme not found"));

            Path uploadDir = Paths.get("uploads/sitemaps");
            Files.createDirectories(uploadDir);

            Path filePath = uploadDir.resolve("sitemap_" + tenantThemeId + ".xml");

            Files.write(filePath, file.getBytes());

            theme.setSitemapPath(filePath.toString());
            tenantThemeRepository.save(theme);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload sitemap");
        }
    }

    // 6️⃣ GET SITEMAP
    @Override
    public byte[] getSitemap(Long tenantThemeId) {

        try {
            TenantTheme theme = tenantThemeRepository.findById(tenantThemeId)
                    .orElseThrow(() -> new RuntimeException("Theme not found"));

            if (theme.getSitemapPath() == null) {
                throw new RuntimeException("Sitemap not uploaded");
            }

            return Files.readAllBytes(Paths.get(theme.getSitemapPath()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to read sitemap");
        }
    }

    // 7️⃣ DELETE SITEMAP
    @Override
    @Transactional
    public void deleteSitemap(Long tenantThemeId) {

        try {
            TenantTheme theme = tenantThemeRepository.findById(tenantThemeId)
                    .orElseThrow(() -> new RuntimeException("Theme not found"));

            if (theme.getSitemapPath() != null) {
                Files.deleteIfExists(Paths.get(theme.getSitemapPath()));
            }

            theme.setSitemapPath(null);
            tenantThemeRepository.save(theme);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete sitemap");
        }
    }
}
