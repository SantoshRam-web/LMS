package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityReport {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long reportId;

private Long threadId;

private Long replyId;

private Long reportedBy;

private String reason;

private String status;

private LocalDateTime createdAt;

}