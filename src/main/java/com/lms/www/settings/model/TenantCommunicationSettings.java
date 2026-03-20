package com.lms.www.settings.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenant_communication_settings")
@Getter
@Setter
public class TenantCommunicationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_communication_setting_id")
    private Long tenantCommunicationSettingId;

    @Column(name = "verification")
    private Boolean verification;

    @Column(name = "communication")
    private Boolean communication;

    @Column(name = "welcome_message")
    private String welcomeMessage;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "reply_to")
    private String replyTo;
}