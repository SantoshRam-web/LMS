package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_mentions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityMention {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "mention_id")
private Long mentionId;

@Column(name = "thread_id")
private Long threadId;

@Column(name = "reply_id")
private Long replyId;

@Column(name = "mentioned_user_id")
private Long mentionedUserId;

@Column(name = "created_at")
private LocalDateTime createdAt;

}