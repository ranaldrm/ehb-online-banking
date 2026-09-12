package com.ehb.banking.dto;

import java.math.BigDecimal;

import com.ehb.banking.Account;
import com.ehb.banking.Currency;


public record AccountResponse (
    String accountNumber,
    Currency currency,
    BigDecimal balance

){

    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getAccountNumber(),account.getCurrency(), account.getBalance());

    }
        
}
