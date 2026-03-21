package com.lms.www.community.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.community.service.CommunityService;

@RestController
@RequestMapping("/website")
public class WebsiteCommunityController {

    private final CommunityService communityService;

    public WebsiteCommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/marketing-community")
    public Map<String, Object> getMarketingCommunity() {
        return communityService.getMarketingCommunity();
    }
}