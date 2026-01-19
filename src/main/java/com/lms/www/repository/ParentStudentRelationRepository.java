package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.Parent;
import com.lms.www.model.ParentStudentRelation;
import com.lms.www.model.Student;

public interface ParentStudentRelationRepository
        extends JpaRepository<ParentStudentRelation, Long> {

    List<ParentStudentRelation> findByParent(Parent parent);
    List<ParentStudentRelation> findByStudent(Student student);
    
    void deleteByStudent_User_UserId(Long userId);

    void deleteByParent_User_UserId(Long userId);

}
