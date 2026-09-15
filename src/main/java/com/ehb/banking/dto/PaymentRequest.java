package com.ehb.banking.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest (
    @NotBlank
    String sourceAccountNumber,
    @NotBlank
    String targetAccountNumber,
    @Positive
    @NotNull
    BigDecimal paymentAmount
    
)
{}
