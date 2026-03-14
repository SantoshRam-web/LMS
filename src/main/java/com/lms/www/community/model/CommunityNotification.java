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
@Column(name = "notification_id")
private Long notificationId;

@Column(name = "user_id")
private Long userId;

@Column(name = "thread_id")
private Long threadId;

@Column(name = "reply_id")
private Long replyId;

private String type;

@Column(name = "is_read")
private Boolean isRead;

@Column(name = "created_at")
private LocalDateTime createdAt;

}