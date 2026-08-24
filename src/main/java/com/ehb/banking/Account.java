package com.ehb.banking;
import java.math.BigDecimal;

import com.ehb.banking.exceptions.BankingException;
import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.exceptions.NonPositiveAmountException;
import java.util.List;
import java.util.ArrayList;

public class Account {
        
    private final String accountNumber;
    private final Currency currency;
    private final List<Transaction> transactions;
    private BigDecimal balance;

    // constructor
    public Account(String accountNumber, Currency currency){
        if (accountNumber  == null || currency == null ) {
            throw new BankingException ("Account number and currency required to create Account");
        }
        
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.transactions = new ArrayList<Transaction>();
        this.balance = BigDecimal.ZERO;

    }


    public List <Transaction> getTransactions (){
        return List.copyOf(transactions);
    }


    public void deposit(BigDecimal amount){
        if ( amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new NonPositiveAmountException("Deposit amount must be greater than 0");
        }
        Transaction transaction = Transaction.of(TransactionType.INCOMING, amount);
        this.transactions.add(transaction);   
        this.balance = this.balance.add(amount);

    }

    public void withdraw(BigDecimal amount){

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new NonPositiveAmountException("Withdrawal amount must be greater than 0");
        }
        if (amount.compareTo(this.balance) > 0){
            throw new ExceedsBalanceException("Withdrawal of " + amount + " exceeds balance of " + this.balance);
        }  

        Transaction transaction = Transaction.of(TransactionType.OUTGOING, amount);
        this.transactions.add(transaction);      

        this.balance = this.balance.subtract(amount);
        
    }



    public BigDecimal getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return "Account{accountNumber='" + accountNumber + "', currency=" + currency + ", balance=" + balance + " transactionCount=" + transactions.size() + "}";
        
    }
        


}