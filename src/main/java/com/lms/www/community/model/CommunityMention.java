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
private Long mentionId;

private Long threadId;

private Long replyId;

private Long mentionedUserId;

private LocalDateTime createdAt;

}