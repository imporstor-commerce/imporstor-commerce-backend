package com.sparta.admin.healthz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/healthz")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }
}