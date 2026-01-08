package com.lms.www.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private Student student;
}
