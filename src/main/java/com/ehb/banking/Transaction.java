package com.ehb.banking;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record Transaction (String identifier, TransactionType transactionType, BigDecimal transactionAmount, LocalDateTime timestamp) {

    public static Transaction of (TransactionType transactionType, BigDecimal transactionAmount) {
        return new Transaction(
            UUID.randomUUID().toString(),
            transactionType,
            transactionAmount,
            LocalDateTime.now()
        );
    }
}