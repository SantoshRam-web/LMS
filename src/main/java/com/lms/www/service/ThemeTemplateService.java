package com.lms.www.service;

import org.springframework.web.multipart.MultipartFile;

public interface ThemeTemplateService {

    void importThemeTemplate(
            MultipartFile file,
            String name,
            String description
    );
}
