package com.lms.www.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.model.Parent;
import com.lms.www.model.ParentStudentRelation;
import com.lms.www.model.Student;
import com.lms.www.model.User;
import com.lms.www.repository.ParentRepository;
import com.lms.www.repository.ParentStudentRelationRepository;
import com.lms.www.repository.UserRepository;

@RestController
@RequestMapping("/parent")
public class ParentController {

    private final ParentRepository parentRepository;
    private final ParentStudentRelationRepository relationRepository;
    private final UserRepository userRepository;

    public ParentController(
            ParentRepository parentRepository,
            ParentStudentRelationRepository relationRepository,
            UserRepository userRepository
    ) {
        this.parentRepository = parentRepository;
        this.relationRepository = relationRepository;
        this.userRepository = userRepository;
    }
    
    @GetMapping("/test")
    public String testParentAccess() {
        return "PARENT ACCESS OK";
    }


    // ✅ GET LOGGED-IN PARENT PROFILE + CHILDREN
    @GetMapping("/me")
    public ParentProfileResponse getMyProfile() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Parent parent = parentRepository.findAll()
                .stream()
                .filter(p -> p.getUser().getUserId().equals(user.getUserId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        List<Student> children = relationRepository.findByParent(parent)
                .stream()
                .map(ParentStudentRelation::getStudent)
                .toList();

        return new ParentProfileResponse(parent, children);
    }

    // 🔹 INNER RESPONSE CLASS (NOT DTO FILE)
    static class ParentProfileResponse {
        public Parent parent;
        public List<Student> children;

        public ParentProfileResponse(Parent parent, List<Student> children) {
            this.parent = parent;
            this.children = children;
        }
    }
}
