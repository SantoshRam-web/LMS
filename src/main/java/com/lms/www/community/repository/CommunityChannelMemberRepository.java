package com.lms.www.community.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.community.model.CommunityChannelMember;

public interface CommunityChannelMemberRepository
        extends JpaRepository<CommunityChannelMember, Long> {

    boolean existsByChannelIdAndUserId(Long channelId, Long userId);
    
    List<CommunityChannelMember> findByUserId(Long userId);
    
    Optional<CommunityChannelMember> findByChannelIdAndUserId(Long channelId, Long userId);
}