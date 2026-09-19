package com.ehb.banking.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.ehb.banking.Payment;



@Service
public class PaymentService {

    private final AccountService accountService;



    public PaymentService (AccountService accountService){
        this.accountService = accountService;
    }

    public Payment processPayment (String sendingAccountNumber, String targetAccountNumber, BigDecimal amount){
           
        Payment payment = accountService.getAccountByNumber(sendingAccountNumber).processOutgoingPayment(amount, accountService.getAccountByNumber(targetAccountNumber));
        return payment;

    }



}