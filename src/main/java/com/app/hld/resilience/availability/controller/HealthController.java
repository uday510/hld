package com.app.hld.resilience.availability.controller;

import com.app.hld.resilience.availability.service.HighlyAvailableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final HighlyAvailableService service;

    public HealthController(HighlyAvailableService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public String checkHealth() {
        if (service.isPrimaryAvailable()) {
            return "Primary service is health";
        }
        return "Primary down - using backup service";
    }
}
