package com.app.hld.resilience.availability.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PrimaryService implements DataService {

    private final Random random = new Random();

    @Override
    public String fetchData() throws Exception {
        if (random.nextInt(10) < 3) {
            throw new Exception("Primary Service Down");
        }
        return "Data from Primary Service";
    }

}
