package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Evaluator;
import com.lms.www.model.User;

@Repository
public interface EvaluatorRepository
        extends JpaRepository<Evaluator, Long> {

    List<Evaluator> findByUser(User user);
}
