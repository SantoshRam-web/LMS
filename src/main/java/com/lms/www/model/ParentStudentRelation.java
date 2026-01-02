package com.lms.www.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parent_student_relation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentStudentRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relId;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
