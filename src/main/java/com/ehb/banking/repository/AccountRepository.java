package com.ehb.banking.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ehb.banking.Account;
import com.ehb.banking.CompositePaymentValidator;
import com.ehb.banking.Currency;
import com.ehb.banking.CurrencyMatchValidator;
import com.ehb.banking.PaymentValidator;
import com.ehb.banking.PositiveAmountValidator;
import com.ehb.banking.SufficientFundsValidator;


@Component
public class AccountRepository {

    private Map<String, Account> accounts;
    private PaymentValidator paymentValidator;


    public AccountRepository () {

        PaymentValidator validator = new CompositePaymentValidator(List.of(
        new PositiveAmountValidator(),
        new SufficientFundsValidator(),
        new CurrencyMatchValidator()
        ));
        
        accounts = new LinkedHashMap<>();
        Account account1 = new Account("1111", Currency.GBP, validator);
        Account account2 = new Account("2222", Currency.EUR, validator);
        Account account3 = new Account("3333", Currency.USD, validator);
        Account account4 = new Account("4444", Currency.GBP, validator);
        Account account5 = new Account("5555", Currency.EUR, validator);
        accounts.put(account1.getAccountNumber(), account1);
        accounts.put(account2.getAccountNumber(), account2);
        accounts.put(account3.getAccountNumber(), account3);
        accounts.put(account4.getAccountNumber(), account4);
        accounts.put(account5.getAccountNumber(), account5);
        

    }


    public Map<String, Account> getAccounts() {
        return Map.copyOf(accounts);
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(this.accounts.get(accountNumber));
    }
    



}