package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityChannel;

import java.util.List;

public interface CommunityChannelRepository extends JpaRepository<CommunityChannel,Long> {

List<CommunityChannel> findBySpaceId(Long spaceId);

}