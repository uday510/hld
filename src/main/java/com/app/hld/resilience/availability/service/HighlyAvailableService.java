package com.app.hld.resilience.availability.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HighlyAvailableService {

    private final PrimaryService primary;
    private final BackupService backup;
    @Getter
    private boolean primaryAvailable = true;

    public String getData() throws Exception {
        try {
            String result = primary.fetchData();
            primaryAvailable = true;
            return result;
        } catch (Exception e) {
            System.out.println("Primary failed: " + e.getMessage());
            System.out.println("Switching to backup...");
            primaryAvailable = false;
            return backup.fetchData();
        }
    }

}
