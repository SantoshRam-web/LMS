package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marketingmanager")
public class MarketingManagerController {

    @GetMapping("/test")
    public String testMarketingManagerAccess() {
        return "MARKETING MANAGER ACCESS OK";
    }
}
