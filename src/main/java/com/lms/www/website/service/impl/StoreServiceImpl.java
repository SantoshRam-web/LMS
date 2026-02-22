package com.lms.www.website.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.www.website.model.TenantSettings;
import com.lms.www.website.repository.TenantSettingsRepository;
import com.lms.www.website.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

    private final TenantSettingsRepository settingsRepository;
    private final JdbcTemplate jdbcTemplate;

    public StoreServiceImpl(TenantSettingsRepository settingsRepository,
                            JdbcTemplate jdbcTemplate) {
        this.settingsRepository = settingsRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object getStoreData() {

        TenantSettings settings = settingsRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        String viewType = settings.getStoreViewType();

        if ("LIST".equalsIgnoreCase(viewType)) {
            return getAllCourses();
        }

        return getCategoryWiseCourses(settings.getStoreConfig());
    }

    private List<Map<String, Object>> getAllCourses() {
        return jdbcTemplate.queryForList(
                "SELECT course_id, title, price FROM courses WHERE enabled = true"
        );
    }

    private Object getCategoryWiseCourses(String configJson) {

        List<Map<String, Object>> response = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(configJson);
            JsonNode categories = root.get("categories");

            if (categories == null) {
                return response;
            }

            for (JsonNode node : categories) {

                Long categoryId = node.get("categoryId").asLong();
                String label = node.get("label").asText();
                String url = node.get("categoryUrl").asText();

                List<Map<String, Object>> courses =
                        jdbcTemplate.queryForList(
                                "SELECT course_id, title, price " +
                                "FROM courses WHERE category_id = ? AND enabled = true LIMIT 8",
                                categoryId
                        );

                Map<String, Object> block = new HashMap<>();
                block.put("label", label);
                block.put("categoryUrl", url);
                block.put("courses", courses);

                response.add(block);
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid store config");
        }

        return response;
    }
}