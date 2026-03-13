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
private Long threadId;

private Long channelId;

private String title;

@Column(columnDefinition="TEXT")
private String content;

private Long authorId;

private String authorName;

private String authorRole;

private String status;

private Boolean isPinned;

private LocalDateTime createdAt;

}