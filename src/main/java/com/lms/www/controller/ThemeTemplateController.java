package com.lms.www.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.www.service.ThemeTemplateService;

@RestController
@RequestMapping("/platform/themes")
public class ThemeTemplateController {

    private final ThemeTemplateService themeTemplateService;

    public ThemeTemplateController(ThemeTemplateService themeTemplateService) {
        this.themeTemplateService = themeTemplateService;
    }

    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> importTheme(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description
    ) {
        themeTemplateService.importThemeTemplate(file, name, description);
        return ResponseEntity.ok("Theme imported successfully");
    }
}

