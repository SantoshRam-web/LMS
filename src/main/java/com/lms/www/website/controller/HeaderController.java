package com.lms.www.website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.www.website.model.TenantHeader;
import com.lms.www.website.service.HeaderService;

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
}