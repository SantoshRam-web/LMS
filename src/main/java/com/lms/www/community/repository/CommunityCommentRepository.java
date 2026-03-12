package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityPost;

public interface CommunityCommentRepository extends JpaRepository<CommunityPost, Long> {
}