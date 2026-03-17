package com.lms.www.community.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="community_channel_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityChannelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;

    @Column(name="channel_id")
    private Long channelId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="joined_at")
    private LocalDateTime joinedAt;
}