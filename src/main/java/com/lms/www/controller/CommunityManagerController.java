package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communitymanager")
public class CommunityManagerController {

    @GetMapping("/test")
    public String testCommunityManagerAccess() {
        return "COMMUNITY MANAGER ACCESS OK";
    }
}
