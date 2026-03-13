package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityMention;

public interface CommunityMentionRepository extends JpaRepository<CommunityMention,Long> {
}