package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_channels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityChannel {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "channel_id")
private Long channelId;

@Column(name = "space_id")
private Long spaceId;

@Column(name = "channel_name")
private String channelName;

@Column(name = "channel_type")
private String channelType;

@Column(name = "created_at")
private LocalDateTime createdAt;

@Column(name = "admins_only")
private Boolean adminsOnly = false;

private String description;

}