package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_threads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityThread {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "thread_id")
private Long threadId;

@Column(name = "channel_id")
private Long channelId;

private String title;

@Column(columnDefinition="TEXT")
private String content;

@Column(name = "author_id")
private Long authorId;

@Column(name = "author_name")
private String authorName;

@Column(name = "author_role")
private String authorRole;

private String status;

@Column(name = "is_pinned")
private Boolean isPinned;

@Column(name = "created_at")
private LocalDateTime createdAt;

}