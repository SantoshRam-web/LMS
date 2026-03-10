package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transportmanager")
public class TransportManagerController {

    @GetMapping("/test")
    public String testTransportManagerAccess() {
        return "TRANSPORT MANAGER ACCESS OK";
    }
}
