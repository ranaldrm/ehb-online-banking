package com.ehb.banking;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record Transaction (String identifier, TransactionType transactionType, BigDecimal amount, LocalDateTime timestamp) {

    public static Transaction of (TransactionType transactionType, BigDecimal amount) {
        return new Transaction(
            UUID.randomUUID().toString(),
            transactionType,
            amount,
            LocalDateTime.now()
        );
    }
}