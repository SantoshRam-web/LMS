package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityBookmark;
import java.util.List;

public interface CommunityBookmarkRepository extends JpaRepository<CommunityBookmark,Long>{

List<CommunityBookmark> findByUserId(Long userId);

}