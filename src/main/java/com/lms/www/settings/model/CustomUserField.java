package com.lms.www.settings.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "custom_user_fields")
@Getter
@Setter
public class CustomUserField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_field_id")
    private Long customFieldId;

    @Column(name = "field_key")
    private String fieldKey;

    @Column(name = "label")
    private String label;

    @Column(name = "type")
    private String type;

    @Column(name = "icon")
    private String icon;

    @Column(name = "display_order")
    private Long displayOrder;
}