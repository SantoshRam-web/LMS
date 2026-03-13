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
private Long reactionId;

private Long threadId;

private Long replyId;

private Long userId;

private String reactionType;

private LocalDateTime createdAt;

}