package com.lms.www.community.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.community.service.CommunityService;
import com.lms.www.model.User;
import com.lms.www.repository.UserRepository;

@RestController
@RequestMapping("/me")
public class MeCommunityController {

    private final CommunityService communityService;
    private final UserRepository userRepository;

    public MeCommunityController(
            CommunityService communityService,
            UserRepository userRepository
    ) {
        this.communityService = communityService;
        this.userRepository = userRepository;
    }

    @GetMapping("/communities")
    public Map<String, Object> getMyCommunities(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        return communityService.getMyCommunities(user.getUserId());
    }
}