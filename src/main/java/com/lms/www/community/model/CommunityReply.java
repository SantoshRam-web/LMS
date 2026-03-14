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
@Column(name = "reply_id")
private Long replyId;

@Column(name = "thread_id")
private Long threadId;

@Column(name = "parent_reply_id")
private Long parentReplyId;

@Column(columnDefinition="TEXT")
private String content;

@Column(name = "author_id")
private Long authorId;

@Column(name = "author_name")
private String authorName;

@Column(name = "author_role")
private String authorRole;

@Column(name = "is_verified")
private Boolean isVerified;

@Column(name = "is_answer")
private Boolean isAnswer;

@Column(name = "created_at")
private LocalDateTime createdAt;

}