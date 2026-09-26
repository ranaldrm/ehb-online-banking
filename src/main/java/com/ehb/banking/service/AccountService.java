package com.ehb.banking.service;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ehb.banking.Account;
import com.ehb.banking.Transaction;
import com.ehb.banking.exceptions.AccountNotFoundException;
import com.ehb.banking.repository.AccountRepository;





@Service
public class AccountService {


    private final AccountRepository accountRepository;


    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    public Account getAccountByNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new AccountNotFoundException(accountNumber + ": account not found"));
        
    }

    public List<Transaction> getTransactionHistory(String accountNumber){
        return getAccountByNumber(accountNumber).getTransactions();
    }

    public Transaction deposit(String accountNumber, BigDecimal amount) {
        Account account = getAccountByNumber(accountNumber);
        return account.deposit(amount);

    }



}