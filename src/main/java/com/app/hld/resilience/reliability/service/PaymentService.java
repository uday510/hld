package com.app.hld.resilience.reliability.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class PaymentService implements PaymentGateway {

    private final Random random = new Random();

    public String processPayment() throws Exception {
        if (random.nextInt(10) < 4) {
            log.error("Primary payment gateway failed!");
        }
        return "Payment processed via Primary Gateway";
    }
}
