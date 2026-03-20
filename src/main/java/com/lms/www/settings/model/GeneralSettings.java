package com.lms.www.settings.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "general_settings")
@Getter
@Setter
public class GeneralSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "general_setting_id")
    private Long generalSettingId;

    @Column(name = "logo", columnDefinition = "LONGTEXT")
    private String logo;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "language")
    private String language;

    @Column(name = "timezone")
    private String timezone;
}