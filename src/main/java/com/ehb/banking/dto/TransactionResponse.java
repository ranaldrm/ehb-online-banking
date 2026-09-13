package com.ehb.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ehb.banking.Transaction;
import com.ehb.banking.TransactionType;

public record TransactionResponse (
    String identifier, 
    TransactionType transactionType, 
    BigDecimal transactionAmount, 
    LocalDateTime timestamp
    ) {
        public static TransactionResponse from (Transaction transaction) {
            return new TransactionResponse(
                transaction.identifier(), 
                transaction.transactionType(),
                transaction.transactionAmount(),
                transaction.timestamp()
                
            );

        }

}



