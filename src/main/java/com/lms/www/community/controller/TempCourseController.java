package com.lms.www.community.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.community.service.CommunityService;

@RestController
@RequestMapping("/api/temp/course")
public class TempCourseController {

    private final CommunityService communityService;

    public TempCourseController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping("/create")
    public String createCourse(
            @RequestParam Long courseId,
            @RequestParam String courseName
    ) {
        communityService.createCourseCommunity(courseId, courseName);
        return "Course community created";
    }

    @PostMapping("/add-student")
    public String addStudent(
            @RequestParam Long courseId,
            @RequestParam Long userId
    ) {
        communityService.addStudentToCourseCommunity(courseId, userId);
        return "Student added to course community";
    }

    @PostMapping("/add-instructor")
    public String addInstructor(
            @RequestParam Long courseId,
            @RequestParam Long userId
    ) {
        communityService.addInstructorToCourseCommunity(courseId, userId);
        return "Instructor added to course community";
    }
}