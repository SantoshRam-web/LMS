package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/affiliate")
public class AffiliateController {

    @GetMapping("/test")
    public String testAffiliateAccess() {
        return "AFFILIATE ACCESS OK";
    }
}
