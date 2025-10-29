package com.app.hld.resilience.reliability.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BackupPaymentGateway implements PaymentGateway {

    public String processPayment() {
        log.info("Processing payment via Backup Gateway...");
        return "Payment processed via Backup Gateway";
    }
}
