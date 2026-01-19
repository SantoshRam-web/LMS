package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.AuditLog;

@Repository
public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {
	
	Optional<AuditLog>
	findTopByEntityNameAndEntityIdAndActionOrderByCreatedTimeDesc(
	        String entityName,
	        Long entityId,
	        String action
	);
	
	void deleteByUserId(Long userId);

}
