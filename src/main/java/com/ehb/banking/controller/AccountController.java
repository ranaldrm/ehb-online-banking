package com.ehb.banking.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehb.banking.Account;
import com.ehb.banking.Transaction;
import com.ehb.banking.dto.AccountResponse;
import com.ehb.banking.dto.TransactionResponse;
import com.ehb.banking.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController (AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(@PathVariable(value="accountNumber") String accountNumber){
        
        Account account = accountService.getAccountByNumber(accountNumber);
        return AccountResponse.from(account);
    }

    @GetMapping("/{accountNumber}/transactions")
    public List<TransactionResponse> getTransactionHistory(@PathVariable(value="accountNumber") String accountNumber) {
        List<Transaction> transactions = accountService.getTransactionHistory(accountNumber);
        return transactions.stream()
                            .map(TransactionResponse::from)
                            .toList();      
    }



}