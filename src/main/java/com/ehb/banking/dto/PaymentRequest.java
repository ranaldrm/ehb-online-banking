package com.ehb.banking.dto;

import java.math.BigDecimal;

public record PaymentRequest (
    String sourceAccountNumber,
    String targetAccountNumber,
    BigDecimal paymentAmount
    
)
{}