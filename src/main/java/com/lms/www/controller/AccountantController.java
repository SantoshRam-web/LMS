package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountant")
public class AccountantController {

    @GetMapping("/test")
    public String testAccountantAccess() {
        return "ACCOUNTANT ACCESS OK";
    }
}
