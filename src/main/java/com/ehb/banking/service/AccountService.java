package com.ehb.banking.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ehb.banking.Account;
import com.ehb.banking.repository.AccountRepository;





@Service
public class AccountService {


    private final AccountRepository accountRepository;


    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    public Optional<Account> getAccountByNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber);
    }



}