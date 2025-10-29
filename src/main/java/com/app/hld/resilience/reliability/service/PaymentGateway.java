package com.app.hld.resilience.reliability.service;

public interface PaymentGateway {
    String processPayment() throws Exception;
}
