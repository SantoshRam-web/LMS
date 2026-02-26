package com.lms.www.website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.www.website.model.TenantHeader;
import com.lms.www.website.service.HeaderService;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/website/header")
public class HeaderController {

    private final HeaderService headerService;

    public HeaderController(HeaderService headerService) {
        this.headerService = headerService;
    }

    // ==============================
    // 1️⃣ Save Custom Header
    // ==============================
    @PostMapping("/save")
    public ResponseEntity<Long> saveHeader(
            @RequestBody String headerConfig) {

        Long headerId = headerService.saveHeader(headerConfig);
        return ResponseEntity.ok(headerId);
    }

    // ==============================
    // 2️⃣ List All Saved Headers
    // ==============================
    @GetMapping("/list")
    public ResponseEntity<List<TenantHeader>> getHeaders() {

        return ResponseEntity.ok(headerService.getHeaders());
    }

    // ==============================
    // 3️⃣ Apply Header To Theme
    // ==============================
    @PostMapping("/apply")
    public ResponseEntity<String> applyHeader(
            @RequestParam Long tenantThemeId,
            @RequestParam Long headerId) {

        headerService.applyHeaderToTheme(tenantThemeId, headerId);
        return ResponseEntity.ok("Header applied successfully");
    }

    // ==============================
    // 4️⃣ Revert To Default Header
    // ==============================
    @PostMapping("/revert")
    public ResponseEntity<String> revertHeader(
            @RequestParam Long tenantThemeId) {

        headerService.revertToDefault(tenantThemeId);
        return ResponseEntity.ok("Reverted to theme default header");
    }
    
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportHeader(@RequestBody Map<String, String> payload) {

        try {

            String html = payload.get("html");
            String css = payload.get("css");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(baos);

            // index.html
            zipOut.putNextEntry(new ZipEntry("index.html"));
            zipOut.write(html.getBytes(StandardCharsets.UTF_8));
            zipOut.closeEntry();

            // styles.css
            zipOut.putNextEntry(new ZipEntry("styles.css"));
            zipOut.write(css.getBytes(StandardCharsets.UTF_8));
            zipOut.closeEntry();

            zipOut.close();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=header.zip")
                    .header("Content-Type", "application/zip")
                    .body(baos.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error exporting header ZIP", e);
        }
    }
}