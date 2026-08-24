package com.ehb.banking;
import java.math.BigDecimal;

public class Account {
        
    private final String accountNumber;
    private final Currency currency;
    private BigDecimal balance;

    // constructor
    public Account(String accountNumber, Currency currency){
        if (accountNumber  == null || currency == null ) {
            throw new IllegalArgumentException ("Account number and currency required");
        }
        
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;

    }

    public void deposit(BigDecimal amount){
        if ( amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount){

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (amount.compareTo(this.balance) > 0){
            throw new IllegalArgumentException("Amount cannot be greater than balance");
        }        

        this.balance = this.balance.subtract(amount);
        
    }



    public BigDecimal getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return "Account{accountNumber='" + accountNumber + "', currency=" + currency + ", balance=" + balance + "}";
        
    }
        


}