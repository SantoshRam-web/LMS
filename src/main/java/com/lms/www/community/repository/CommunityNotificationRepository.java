package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityNotification;

import java.util.List;

public interface CommunityNotificationRepository extends JpaRepository<CommunityNotification,Long> {

List<CommunityNotification> findByUserId(Long userId);

}