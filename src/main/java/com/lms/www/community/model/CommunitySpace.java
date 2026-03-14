package com.lms.www.community.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="community_spaces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunitySpace {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "space_id")
private Long spaceId;

@Column(name = "space_name")
private String spaceName;

@Column(name = "course_id")
private String courseId;

@Column(name = "created_at")
private LocalDateTime createdAt;

}