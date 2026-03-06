package com.lms.www.website.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
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

            Path themeRoot = Files.list(tempDir)
                    .filter(Files::isDirectory)
                    .findFirst()
                    .orElse(tempDir);

            String rootFolder = themeRoot.getFileName().toString();

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

            Path themeStorage = Path.of("uploads/themes/" + themeId);

            Files.createDirectories(themeStorage);

            /*
             * 🔥 FIX 1
             * Copy ONLY themeRoot (not tempDir)
             */
            Files.walk(themeRoot).forEach(source -> {

                try {

                    Path destination =
                            themeStorage.resolve(themeRoot.relativize(source));

                    if (Files.isDirectory(source)) {

                        Files.createDirectories(destination);

                    } else {

                        Files.copy(
                                source,
                                destination,
                                StandardCopyOption.REPLACE_EXISTING
                        );
                    }

                } catch (IOException e) {
                    throw new RuntimeException("Asset copy failed", e);
                }

            });

            /*
             * PREVIEW IMAGE
             */

            Path previewImage = Files.walk(themeRoot)
                    .filter(p -> {
                        String n = p.getFileName().toString().toLowerCase();
                        return n.contains("preview")
                                || n.contains("screenshot")
                                || n.endsWith(".jpg")
                                || n.endsWith(".png");
                    })
                    .findFirst()
                    .orElse(null);

            if (previewImage != null) {

                Path previewTarget =
                        Path.of("uploads/themes/" + themeId + "/preview.jpg");

                Files.copy(
                        previewImage,
                        previewTarget,
                        StandardCopyOption.REPLACE_EXISTING
                );

                jdbcTemplate.update(
                        "UPDATE theme_templates SET preview_image_url=? WHERE theme_id=?",
                        "/themes/" + themeId + "/preview.jpg",
                        themeId
                );
            }

            ObjectMapper objectMapper = new ObjectMapper();

            List<Path> htmlFiles = Files.walk(themeRoot)
                    .filter(p -> p.toString().endsWith(".html"))
                    .toList();

            if (htmlFiles.isEmpty()) {
                throw new RuntimeException("No HTML pages found");
            }

            for (Path htmlPath : htmlFiles) {

                String fileName = htmlPath.getFileName().toString();

                String pageKey;
                String slug;

                if (fileName.equalsIgnoreCase("index.html")) {

                    pageKey = "HOME";
                    slug = "/";

                } else {

                    String base = fileName.replace(".html", "");

                    pageKey = base.replace("-", "_").toUpperCase();
                    slug = "/" + base.toLowerCase();
                }

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

                Document doc = Jsoup.parse(htmlPath.toFile(), "UTF-8");

                rewriteAssetPaths(doc, themeId);

                List<Element> sections = detectSections(doc);

                int order = 1;

                for (Element section : sections) {

                    String htmlContent = section.outerHtml();

                    String jsonConfig = objectMapper.writeValueAsString(
                            Map.of("html", htmlContent)
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

    /*
     * 🔥 FIX 2
     * Rewrite ALL asset paths correctly
     */

    private void rewriteAssetPaths(Document doc, Long themeId) {

        Elements assets = doc.select("[src], [href]");

        for (Element el : assets) {

            String attr = el.hasAttr("src") ? "src" : "href";

            String val = el.attr(attr);

            if (val.startsWith("http")
                    || val.startsWith("https")
                    || val.startsWith("data:")
                    || val.startsWith("#")
                    || val.startsWith("mailto:")
                    || val.startsWith("tel:")
                    || val.startsWith("/themes/")) {
                continue;
            }

            el.attr(attr, "/themes/" + themeId + "/" + val);
        }
    }

    private List<Element> detectSections(Document doc) {

        List<Element> sections = new ArrayList<>();

        Elements found = doc.select("section");

        if (!found.isEmpty()) {
            sections.addAll(found);
            return sections;
        }

        found = doc.select("div.container, div.container-fluid");

        if (!found.isEmpty()) {
            sections.addAll(found);
            return sections;
        }

        for (Element child : doc.body().children()) {

            if (!child.tagName().equalsIgnoreCase("script")
                    && !child.tagName().equalsIgnoreCase("style")) {

                sections.add(child);
            }
        }

        return sections;
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