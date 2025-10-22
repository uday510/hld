package com.app.hld.resilience.availability.controller;

import com.app.hld.resilience.availability.service.HighlyAvailableService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private final HighlyAvailableService service;

    public DataController(HighlyAvailableService service) {
        this.service = service;
    }

    @GetMapping("/data")
    public String getData() throws Exception {
        return service.getData();
    }
}
