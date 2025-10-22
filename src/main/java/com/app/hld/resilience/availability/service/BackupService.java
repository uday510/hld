package com.app.hld.resilience.availability.service;

import org.springframework.stereotype.Service;

@Service
public class BackupService implements DataService {

    @Override
    public String fetchData() throws Exception {
        return "Data from Backup Service";
    }
}