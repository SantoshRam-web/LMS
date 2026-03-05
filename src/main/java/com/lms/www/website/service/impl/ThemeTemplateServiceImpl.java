package com.lms.www.website.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
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
            
            System.out.println("===== Extracted Theme Structure =====");

            Files.walk(tempDir).forEach(path -> {
                System.out.println(path.toString());
            });

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

            ObjectMapper objectMapper = new ObjectMapper();

            // 2️⃣ Find ALL HTML files inside zip
            List<Path> htmlFiles = Files.walk(tempDir)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".html"))
                    .toList();

            if (htmlFiles.isEmpty()) {
                throw new RuntimeException("No HTML pages found in theme");
            }

            for (Path htmlPath : htmlFiles) {

                String fileName = htmlPath.getFileName().toString().toLowerCase();

                // Generate page_key
                String pageKey;
                String slug; // ✅ Added

                if (fileName.equals("index.html")) {
                    pageKey = "HOME";
                    slug = "/"; // ✅ Added
                } else {
                    String baseName = fileName.replace(".html", "");

                    pageKey = baseName
                            .replace("-", "_")
                            .toUpperCase();

                    slug = "/" + baseName.toLowerCase(); // ✅ Added
                }

                // Insert page (UPDATED — now includes slug)
                jdbcTemplate.update(
                        "INSERT INTO theme_template_pages (theme_id, page_key, slug) VALUES (?,?,?)",
                        themeId,
                        pageKey,
                        slug
                );

                Long templatePageId = jdbcTemplate.queryForObject(
                        "SELECT MAX(template_page_id) FROM theme_template_pages",
                        Long.class
                );

                // Parse HTML
                Document doc = Jsoup.parse(htmlPath.toFile(), "UTF-8");

                Elements sections = doc.select("body > section, body > div.container-fluid");

                int order = 1;

                for (Element section : sections) {

                	String htmlContent = section.outerHtml();

                	htmlContent = htmlContent
                	        .replace("href=\"index.html\"", "href=\"/\"")
                	        .replace("href=\"about.html\"", "href=\"/about\"")
                	        .replace("href=\"course.html\"", "href=\"/course\"")
                	        .replace("href=\"menu.html\"", "href=\"/course\"")
                	        .replace("href=\"team.html\"", "href=\"/team\"")
                	        .replace("href=\"testimonial.html\"", "href=\"/testimonial\"")
                	        .replace("href=\"contact.html\"", "href=\"/contact\"")
                	        .replace("src=\"img/", "src=\"/themes/" + themeId + "/img/")
                	        .replace("href=\"css/", "href=\"/themes/" + themeId + "/css/")
                	        .replace("src=\"js/", "src=\"/themes/" + themeId + "/js/")
                	        .replace("href=\"assets/", "href=\"/themes/" + themeId + "/assets/")
                	        .replace("src=\"assets/", "src=\"/themes/" + themeId + "/assets/")
                	        .replace("src=\"images/", "src=\"/themes/" + themeId + "/images/")
                	        .replace("href=\"lib/", "href=\"/themes/" + themeId + "/lib/")
                	        .replace("src=\"lib/", "src=\"/themes/" + themeId + "/lib/");

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
