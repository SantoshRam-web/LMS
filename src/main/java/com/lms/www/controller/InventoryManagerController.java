package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventorymanager")
public class InventoryManagerController {

    @GetMapping("/test")
    public String testInventoryManagerAccess() {
        return "INVENTORY MANAGER ACCESS OK";
    }
}
