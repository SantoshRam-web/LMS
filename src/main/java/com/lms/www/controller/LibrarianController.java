package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/librarian")
public class LibrarianController {

    @GetMapping("/test")
    public String testLibrarianAccess() {
        return "LIBRARIAN ACCESS OK";
    }
}
