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
@Column(name = "report_id")
private Long reportId;

@Column(name = "thread_id")
private Long threadId;

@Column(name = "reply_id")
private Long replyId;

@Column(name = "reported_by")
private Long reportedBy;

private String reason;

private String status;

@Column(name = "created_at")
private LocalDateTime createdAt;

}