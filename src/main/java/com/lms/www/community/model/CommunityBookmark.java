package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_bookmarks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityBookmark {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "bookmark_id")
private Long bookmarkId;

@Column(name = "thread_id")
private Long threadId;

@Column(name = "user_id")
private Long userId;

@Column(name = "created_at")
private LocalDateTime createdAt;

}