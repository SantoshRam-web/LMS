package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityPost;

public interface CommunityChannelRepository extends JpaRepository<CommunityPost, Long> {
}