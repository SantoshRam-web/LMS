package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_reactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityReaction {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "reaction_id")
private Long reactionId;

@Column(name = "thread_id")
private Long threadId;

@Column(name = "reply_id")
private Long replyId;

@Column(name = "user_id")
private Long userId;

@Column(name = "reaction_type")
private String reactionType;

@Column(name = "created_at")
private LocalDateTime createdAt;

}