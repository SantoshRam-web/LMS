package com.lms.www.website.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.www.website.service.StoreService;

@RestController
@RequestMapping("/website/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<?> getStoreData() {
        return ResponseEntity.ok(storeService.getStoreData());
    }
}