package com.app.hld.resilience.reliability.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReliabilityPaymentService {

    private final PaymentService primary;
    private final BackupPaymentGateway backup;

    private static final String SERVICE_NAME = "paymentService";

    @Retry(name = SERVICE_NAME, fallbackMethod = "fallbackPayment")
    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackPayment")
    public String makePayment() throws Exception {
        log.info("Attempting to process payment...");
        return primary.processPayment();
    }

    public String fallbackPayment(Exception ex) {
        log.warn("Fallback triggered due to: {}", ex.getMessage());
        return backup.processPayment();
    }
}
