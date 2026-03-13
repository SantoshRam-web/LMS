package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityNotification {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long notificationId;

private Long userId;

private Long threadId;

private Long replyId;

private String type;

private Boolean isRead;

private LocalDateTime createdAt;

}