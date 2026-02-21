package com.lms.www.website.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lms.www.website.model.TenantSettings;
import com.lms.www.website.repository.TenantSettingsRepository;
import com.lms.www.website.service.SettingsService;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final TenantSettingsRepository repository;

    private static final String BASE_UPLOAD_DIR = "uploads/";

    public SettingsServiceImpl(TenantSettingsRepository repository) {
        this.repository = repository;
    }

    // Always fetch first row (one row per tenant DB)
    private TenantSettings getOrCreateSettings() {

        return repository.findAll().stream().findFirst().orElseGet(() -> {
            TenantSettings settings = new TenantSettings();
            settings.setFootfallEnabled(false);
            settings.setStoreViewType("LIST");
            return repository.save(settings);
        });
    }

    @Override
    @Transactional
    public void updateSiteName(String siteName) {

        TenantSettings settings = getOrCreateSettings();
        settings.setSiteName(siteName);
        repository.save(settings);
    }

    @Override
    @Transactional
    public void uploadLogo(MultipartFile file) {

        try {
            TenantSettings settings = getOrCreateSettings();

            Path uploadDir = Paths.get(BASE_UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            Path filePath = uploadDir.resolve("logo_" + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            settings.setLogoPath(filePath.toString());
            repository.save(settings);

        } catch (IOException e) {
            throw new RuntimeException("Logo upload failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void uploadFavicon(MultipartFile file) {

        try {
            TenantSettings settings = getOrCreateSettings();

            Path uploadDir = Paths.get(BASE_UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            Path filePath = uploadDir.resolve("favicon_" + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            settings.setFaviconPath(filePath.toString());
            repository.save(settings);

        } catch (IOException e) {
            throw new RuntimeException("Favicon upload failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateFootfall(Boolean enabled) {

        TenantSettings settings = getOrCreateSettings();
        settings.setFootfallEnabled(enabled);
        repository.save(settings);
    }

    @Override
    @Transactional
    public void updateStoreTheme(String viewType, String configJson) {

        TenantSettings settings = getOrCreateSettings();
        settings.setStoreViewType(viewType);
        settings.setStoreConfig(configJson);
        repository.save(settings);
    }

    @Override
    public TenantSettings getSettings() {
        return getOrCreateSettings();
    }
}
