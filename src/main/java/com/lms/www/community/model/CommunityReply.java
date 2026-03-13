package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_replies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityReply {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long replyId;

private Long threadId;

private Long parentReplyId;

@Column(columnDefinition="TEXT")
private String content;

private Long authorId;

private String authorName;

private String authorRole;

private Boolean isVerified;

private Boolean isAnswer;

private LocalDateTime createdAt;

}