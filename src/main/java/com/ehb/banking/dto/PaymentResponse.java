package com.ehb.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ehb.banking.Currency;
import com.ehb.banking.Payment;
import com.ehb.banking.PaymentStatus;



public record PaymentResponse (
    String sourceAccountNumber,
    String targetAccountNumber,
    BigDecimal paymentAmount,
    PaymentStatus paymentStatus,
    String paymentID,
    LocalDateTime paymentTime,
    Currency currency
    
)
{

    public static PaymentResponse from (Payment payment) {
        return new PaymentResponse (
            payment.getSourceAccountNumber(),
            payment.getTargetAccountNumber(),
            payment.getPaymentAmount(),
            payment.getPaymentStatus(),
            payment.getPaymentID(),
            payment.getPaymentTime(),
            payment.getCurrency()
        );
    }
}
