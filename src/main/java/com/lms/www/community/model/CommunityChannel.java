package com.lms.www.community.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "community_channels")
public class CommunityChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelId;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private CommunitySpace space;

    private String name;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public CommunitySpace getSpace() {
        return space;
    }

    public void setSpace(CommunitySpace space) {
        this.space = space;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}