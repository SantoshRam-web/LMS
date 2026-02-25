package com.lms.www.website.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.website.model.TenantHeader;
import com.lms.www.website.model.TenantTheme;
import com.lms.www.website.repository.TenantHeaderRepository;
import com.lms.www.website.repository.TenantThemeRepository;
import com.lms.www.website.service.HeaderService;

@Service
public class HeaderServiceImpl implements HeaderService {

    private final TenantHeaderRepository headerRepository;
    private final TenantThemeRepository themeRepository;

    public HeaderServiceImpl(TenantHeaderRepository headerRepository,
                             TenantThemeRepository themeRepository) {
        this.headerRepository = headerRepository;
        this.themeRepository = themeRepository;
    }

    // ==============================
    // 1️⃣ Save Custom Header
    // ==============================
    @Override
    public Long saveHeader(String headerConfig) {

        TenantHeader header = new TenantHeader();
        header.setHeaderConfig(headerConfig);

        headerRepository.save(header);

        return header.getTenantHeaderId();
    }

    // ==============================
    // 2️⃣ List All Custom Headers
    // ==============================
    @Override
    public List<TenantHeader> getHeaders() {
        return headerRepository.findAllByOrderByCreatedAtDesc();
    }

    // ==============================
    // 3️⃣ Apply Header To Theme
    // ==============================
    @Override
    @Transactional
    public void applyHeaderToTheme(Long tenantThemeId, Long headerId) {

        TenantTheme theme = themeRepository.findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        TenantHeader header = headerRepository.findById(headerId)
                .orElseThrow(() -> new RuntimeException("Header not found"));

        theme.setHeaderConfig(header.getHeaderConfig());
        themeRepository.save(theme);
    }

    // ==============================
    // 4️⃣ Revert To Default Header
    // ==============================
    @Override
    @Transactional
    public void revertToDefault(Long tenantThemeId) {

        TenantTheme theme = themeRepository.findById(tenantThemeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        if (theme.getDefaultHeaderConfig() == null) {
            throw new RuntimeException("Default header not available");
        }

        theme.setHeaderConfig(theme.getDefaultHeaderConfig());
        themeRepository.save(theme);
    }
}