package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityReaction;

public interface CommunityReactionRepository extends JpaRepository<CommunityReaction,Long> {
}