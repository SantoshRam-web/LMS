package com.lms.www.website.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.lms.www.website.model.TenantSettings;
import com.lms.www.website.service.SettingsService;

@RestController
@RequestMapping("/website/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PutMapping("/site-name")
    public ResponseEntity<?> updateSiteName(@RequestBody String siteName) {

        settingsService.updateSiteName(siteName.replace("\"", ""));
        return ResponseEntity.ok(Map.of("message", "Site name updated"));
    }

    @PostMapping("/logo")
    public ResponseEntity<?> uploadLogo(@RequestParam("file") MultipartFile file) {

        settingsService.uploadLogo(file);
        return ResponseEntity.ok(Map.of("message", "Logo uploaded"));
    }

    @PostMapping("/favicon")
    public ResponseEntity<?> uploadFavicon(@RequestParam("file") MultipartFile file) {

        settingsService.uploadFavicon(file);
        return ResponseEntity.ok(Map.of("message", "Favicon uploaded"));
    }

    @PutMapping("/footfall")
    public ResponseEntity<?> updateFootfall(@RequestBody Boolean enabled) {

        settingsService.updateFootfall(enabled);
        return ResponseEntity.ok(Map.of("message", "Footfall updated"));
    }

    @PutMapping("/store")
    public ResponseEntity<?> updateStoreTheme(
            @RequestParam String viewType,
            @RequestBody(required = false) String storeConfigJson) {

        settingsService.updateStoreTheme(viewType, storeConfigJson);
        return ResponseEntity.ok(Map.of("message", "Store theme updated"));
    }

    @GetMapping
    public ResponseEntity<TenantSettings> getSettings() {

        return ResponseEntity.ok(settingsService.getSettings());
    }
}
