package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @GetMapping("/test")
    public String testMentorAccess() {
        return "MENTOR ACCESS OK";
    }
}
