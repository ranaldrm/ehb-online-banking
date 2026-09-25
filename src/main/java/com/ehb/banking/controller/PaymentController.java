package com.ehb.banking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehb.banking.Payment;
import com.ehb.banking.dto.PaymentRequest;
import com.ehb.banking.dto.PaymentResponse;
import com.ehb.banking.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse postPayment (@Valid @RequestBody PaymentRequest paymentRequest ){
        
        Payment payment = paymentService.processPayment(paymentRequest.sourceAccountNumber(), paymentRequest.targetAccountNumber(), paymentRequest.paymentAmount());
        return PaymentResponse.from(payment);
    }



}