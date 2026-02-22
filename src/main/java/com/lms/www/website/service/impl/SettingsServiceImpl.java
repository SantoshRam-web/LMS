package com.lms.www.website.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.www.website.model.TenantSettings;
import com.lms.www.website.repository.TenantSettingsRepository;
import com.lms.www.website.service.SettingsService;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final TenantSettingsRepository repository;
    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_UPLOAD_DIR = "uploads/";

    public SettingsServiceImpl(TenantSettingsRepository repository,
    		JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
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
    public void updateStoreTheme(String viewType, String storeConfigJson) {

        TenantSettings settings = getOrCreateSettings();

        // Validate viewType
        if (!"LIST".equalsIgnoreCase(viewType) &&
            !"CATEGORY".equalsIgnoreCase(viewType)) {
            throw new RuntimeException("Invalid store view type");
        }

        // If category view, validate category IDs
        if ("CATEGORY".equalsIgnoreCase(viewType) && storeConfigJson != null) {

            // Very simple validation (no DTO)
            List<Long> categoryIds = jdbcTemplate.queryForList(
                    "SELECT category_id FROM categories",
                    Long.class
            );

            for (Long id : extractCategoryIds(storeConfigJson)) {
                if (!categoryIds.contains(id)) {
                    throw new RuntimeException("Invalid category id: " + id);
                }
            }
        }

        settings.setStoreViewType(viewType.toUpperCase());
        settings.setStoreConfig(storeConfigJson);

        repository.save(settings);
    }
    
    private List<Long> extractCategoryIds(String json) {

        List<Long> ids = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode categories = root.get("categories");

            if (categories != null && categories.isArray()) {
                for (JsonNode node : categories) {
                    ids.add(node.get("categoryId").asLong());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid store config JSON");
        }

        return ids;
    }


    @Override
    public TenantSettings getSettings() {
        return getOrCreateSettings();
    }
}
