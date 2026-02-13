package com.lms.www.service.Impl;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.www.service.ThemeTemplateService;

@Service
public class ThemeTemplateServiceImpl implements ThemeTemplateService {

    private final JdbcTemplate jdbcTemplate;

    public ThemeTemplateServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void importThemeTemplate(
            MultipartFile file,
            String name,
            String description
    ) {

        try {
            Path tempDir = Files.createTempDirectory("theme-import");

            unzip(file.getInputStream(), tempDir);

            // 1️⃣ Insert theme template
            jdbcTemplate.update(
                    "INSERT INTO theme_templates (name, description, version) VALUES (?,?,?)",
                    name,
                    description,
                    "1.0"
            );

            Long themeId = jdbcTemplate.queryForObject(
                    "SELECT MAX(theme_id) FROM theme_templates",
                    Long.class
            );

            // 2️⃣ Parse index.html (Homepage)
            Path indexPath = Files.walk(tempDir)
                    .filter(p -> p.getFileName().toString().equalsIgnoreCase("index.html"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("index.html not found"));

            Document doc = Jsoup.parse(indexPath.toFile(), "UTF-8");

            jdbcTemplate.update(
                    "INSERT INTO theme_template_pages (theme_id, page_key) VALUES (?,?)",
                    themeId,
                    "HOME"
            );

            Long templatePageId = jdbcTemplate.queryForObject(
                    "SELECT MAX(template_page_id) FROM theme_template_pages",
                    Long.class
            );

            // 3️⃣ Extract sections (based on <section> tags)
            Elements sections = doc.select("section");

            int order = 1;

            ObjectMapper objectMapper = new ObjectMapper();

            for (Element section : sections) {

                String htmlContent = section.outerHtml();

                // Convert HTML into valid JSON
                String jsonConfig = objectMapper.writeValueAsString(
                        java.util.Map.of("html", htmlContent)
                );

                jdbcTemplate.update(
                        "INSERT INTO theme_template_sections " +
                                "(template_page_id, section_type, default_config, display_order) " +
                                "VALUES (?,?,?,?)",
                        templatePageId,
                        "CUSTOM",
                        jsonConfig,
                        order++
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Theme import failed: " + e.getMessage());
        }
    }

    private void unzip(InputStream zipStream, Path targetDir) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(zipStream)) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                Path newPath = targetDir.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
