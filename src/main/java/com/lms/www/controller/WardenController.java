package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/warden")
public class WardenController {

    @GetMapping("/test")
    public String testWardenAccess() {
        return "WARDEN ACCESS OK";
    }
}
