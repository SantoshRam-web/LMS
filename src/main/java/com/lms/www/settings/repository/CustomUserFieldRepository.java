package com.lms.www.settings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.www.settings.model.CustomUserField;

public interface CustomUserFieldRepository extends JpaRepository<CustomUserField, Long> {
    List<CustomUserField> findAllByOrderByDisplayOrderAsc();
}