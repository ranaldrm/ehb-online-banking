package com.ehb.banking.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ehb.banking.Account;
import com.ehb.banking.Currency;


@Component
public class AccountRepository {

    private Map<String, Account> accounts;


    public AccountRepository () {
        accounts = new LinkedHashMap<>();
        Account account1 = new Account("1111", Currency.GBP);
        Account account2 = new Account("2222", Currency.EUR);
        Account account3 = new Account("3333", Currency.USD);
        Account account4 = new Account("4444", Currency.GBP);
        Account account5 = new Account("5555", Currency.EUR);
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