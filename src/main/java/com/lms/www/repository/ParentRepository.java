package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.Parent;

public interface ParentRepository extends JpaRepository<Parent, Long> {
}
