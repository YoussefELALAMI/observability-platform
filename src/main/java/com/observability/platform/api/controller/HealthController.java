package com.observability.platform.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/ping")
    public Map<String, String> ping(){
        return Map.of(
                "status", "ok",
                "service", "observability-platform",
                "version", "1.0.0"
        );
    }
}
