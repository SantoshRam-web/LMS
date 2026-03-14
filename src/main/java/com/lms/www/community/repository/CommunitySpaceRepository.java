package com.lms.www.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.community.model.CommunitySpace;

public interface CommunitySpaceRepository extends JpaRepository<CommunitySpace,Long> {
	List<CommunitySpace> findBySpaceNameContainingIgnoreCase(String name);
}