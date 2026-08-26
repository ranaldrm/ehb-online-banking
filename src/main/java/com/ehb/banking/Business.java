package com.ehb.banking;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.ehb.banking.exceptions.AccountNotFoundException;
import com.ehb.banking.exceptions.DuplicateAccountNumberException;



public class Business {

    private final Map<String, Account> accountMap;
    private final String businessID;
    private String businessName;
//this password is not included in the tickets but I want to include it so that at some later stage I can 
//implement a simple UI and allow businesses to "login" and make payments etc.
    private String password;


    public Business (String businessName, String password) {

        this.accountMap = new HashMap<>();
        this.businessName = businessName;
        this.businessID = UUID.randomUUID().toString();
        this.password = password;
    }

    public String getBusinessName () {
        return this.businessName;
    }

    public String getBusinessID () {
        return this.businessID;
    }

    public Map <String, Account> getAllAccounts () {
        return Map.copyOf(this.accountMap);
    }


    // Returns Optional — caller decides what "not found" means
    public Optional<Account> findAccount(String accountNumber) {
        return Optional.ofNullable(this.accountMap.get(accountNumber));
    }

    // Convenience method — throws if not found, for when absence is definitely an error
    public Account getAccount(String accountNumber) {
        return findAccount(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                    "Account: " + accountNumber + " not found belonging to: " + this.businessName));
    }


    public void addAccount(Account account) {
        if (accountMap.containsKey(account.getAccountNumber()) ){
            throw new DuplicateAccountNumberException("account with number: " + account.getAccountNumber() + " already exists.");
        }
        else {
            this.accountMap.put(account.getAccountNumber(), account);
        }
       
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() ){
            return false;
        }

        Business other = (Business) obj;

        return Objects.equals(this.getBusinessID(),other.getBusinessID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessID);
    }




}