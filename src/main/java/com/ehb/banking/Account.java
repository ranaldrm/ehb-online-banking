package com.ehb.banking;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ehb.banking.exceptions.BankingException;
import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.exceptions.NonPositiveAmountException;

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
        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.ZERO;

    }

    public String getAccountNumber(){
        return this.accountNumber;
    }

    public List <Transaction> getTransactions (){

        return List.copyOf(transactions);
    }

    public List <Transaction> getOutgoingTransactions(){

        return this.transactions.stream()
        .filter(transaction -> transaction.transactionType() == TransactionType.OUTGOING)
        .collect(Collectors.toList());   
    }

    public List<Transaction> getIncomingTransactions(){
        return this.transactions.stream()
        .filter(transaction -> transaction.transactionType() == TransactionType.INCOMING)
        .collect(Collectors.toList());

    }

    public BigDecimal getTotalOutgoingPayments() {
        return this.transactions.stream()
        .filter(transaction -> transaction.transactionType() == TransactionType.OUTGOING)
        .map(Transaction::amount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalIncomingTransactionSum() {
        return this.transactions.stream()
        .filter(transaction -> transaction.transactionType() == TransactionType.INCOMING)
        .map(Transaction::amount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
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