package com.lms.www.community.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.community.model.CommunityChannel;

public interface CommunityChannelRepository extends JpaRepository<CommunityChannel,Long> {

List<CommunityChannel> findBySpaceId(Long spaceId);

Optional<CommunityChannel> findBySpaceIdAndChannelName(Long spaceId, String channelName);
}