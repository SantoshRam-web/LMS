package com.lms.www.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.community.model.CommunityReport;

public interface CommunityReportRepository extends JpaRepository<CommunityReport,Long> {
}