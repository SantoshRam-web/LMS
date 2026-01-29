package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conductor")
public class ConductorController {

    @GetMapping("/test")
    public String testConductorAccess() {
        return "CONDUCTOR ACCESS OK";
    }
}
