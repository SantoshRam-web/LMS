package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityReply;
import java.util.List;

public interface CommunityReplyRepository extends JpaRepository<CommunityReply,Long>{

List<CommunityReply> findByThreadIdOrderByCreatedAtAsc(Long threadId);

}