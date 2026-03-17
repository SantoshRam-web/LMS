package com.lms.www.community.controller;

import com.lms.www.model.User;
import com.lms.www.repository.UserRepository;
import com.lms.www.community.service.CommunityService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final UserRepository userRepository;
    private final CommunityService communityService;

    public LeadController(
            UserRepository userRepository,
            CommunityService communityService
    ){
        this.userRepository = userRepository;
        this.communityService = communityService;
    }

    @PostMapping
    public User createLead(@RequestBody User user){

        user.setRoleName("ROLE_LEAD");

        User savedUser = userRepository.save(user);

        communityService.autoJoinMarketingChannel(
                savedUser.getUserId()
        );

        return savedUser;
    }
}