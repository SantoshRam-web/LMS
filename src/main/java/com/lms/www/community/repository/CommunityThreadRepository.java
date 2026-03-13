package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityThread;
import java.util.List;

public interface CommunityThreadRepository extends JpaRepository<CommunityThread,Long>{

List<CommunityThread> findByChannelId(Long channelId);

}