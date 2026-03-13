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
private Long bookmarkId;

private Long threadId;

private Long userId;

private LocalDateTime createdAt;

}