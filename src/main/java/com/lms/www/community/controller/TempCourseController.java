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

    // 1️⃣ Create course → create community
    @PostMapping("/create")
    public String createCourse(
            @RequestParam Long courseId,
            @RequestParam String courseName
    ){
        communityService.createCourseCommunity(courseId, courseName);
        return "Course community created";
    }

    // 2️⃣ Enroll user → add to community
    @PostMapping("/enroll")
    public String enrollUser(
            @RequestParam Long courseId,
            @RequestParam Long userId,
            @RequestParam String roleName
    ){
        communityService.addUserToCourseCommunity(courseId, userId, roleName);
        return "User added to course community";
    }
}