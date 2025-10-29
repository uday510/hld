package com.app.hld.resilience.reliability.controller;


import com.app.hld.resilience.reliability.service.ReliabilityPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final ReliabilityPaymentService paymentService;

    @GetMapping("/pay")
    public String processPayment() throws Exception {
        return paymentService.makePayment();
    }

}
