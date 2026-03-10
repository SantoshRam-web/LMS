package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evaluator")
public class EvaluatorController {

    @GetMapping("/test")
    public String testEvaluatorAccess() {
        return "EVALUATOR ACCESS OK";
    }
}
