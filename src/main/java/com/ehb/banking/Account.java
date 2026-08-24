package com.ehb.banking;
import java.math.BigDecimal;

import com.ehb.banking.exceptions.BankingException;
import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.exceptions.NonPositiveAmountException;

public class Account {
        
    private final String accountNumber;
    private final Currency currency;
    private BigDecimal balance;

    // constructor
    public Account(String accountNumber, Currency currency){
        if (accountNumber  == null || currency == null ) {
            throw new BankingException ("Account number and currency required to create Account");
        }
        
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;

    }

    public void deposit(BigDecimal amount){
        if ( amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new NonPositiveAmountException("Deposit amount must be greater than 0");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount){

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new NonPositiveAmountException("Withdrawal amount must be greater than 0");
        }
        if (amount.compareTo(this.balance) > 0){
            throw new ExceedsBalanceException("Withdrawal of" + amount + "exceeds balance of " + this.balance);
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