package com.ehb.banking;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ehb.banking.exceptions.BankingException;
import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.exceptions.NonPositiveAmountException;

public class Account {
        
    private final String accountNumber;
    private final Currency currency;
    private final List<Transaction> transactions;
    private BigDecimal balance;
    private final PaymentValidator paymentValidator;

    public Account(String accountNumber, Currency currency) {
        this(accountNumber, currency, null);
    }

    public Account(String accountNumber, Currency currency, PaymentValidator paymentValidator) {
        if (accountNumber == null || currency == null) {
            throw new BankingException("Account number and currency required to create Account");
        }
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.ZERO;
        this.paymentValidator = paymentValidator;
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
        .map(Transaction::transactionAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalIncomingTransactionSum() {
        return this.transactions.stream()
        .filter(transaction -> transaction.transactionType() == TransactionType.INCOMING)
        .map(Transaction::transactionAmount)
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

    public void processIncomingPayment(Payment payment) {
        BigDecimal amount = payment.getPaymentAmount();
        this.balance = this.balance.add(amount);
        Transaction transaction = Transaction.of(TransactionType.INCOMING, amount);
        this.transactions.add(transaction);
    }

    
    public void processOutgoingPayment(BigDecimal paymentAmount, Account targetAccount) {
        if (paymentValidator == null) {
            throw new BankingException("No payment validator configured for account " + accountNumber);
        }
        
        Payment payment = new Payment(paymentAmount, this.getAccountNumber(), targetAccount.getAccountNumber());
        paymentValidator.validate(payment, this);
        payment.validate();
        payment.approve();
        Transaction outGoingTransaction = Transaction.of(TransactionType.OUTGOING, payment.getPaymentAmount());
        this.transactions.add(outGoingTransaction);
        this.balance = this.balance.subtract(payment.getPaymentAmount());
        targetAccount.processIncomingPayment(payment);
        payment.complete();
    }


    public BigDecimal getBalance() {
        return this.balance;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    @Override
    public String toString() {
        return "Account{accountNumber='" + accountNumber + "', currency=" + currency + ", balance=" + balance + " transactionCount=" + transactions.size() + "}";
        
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Account other = (Account) obj;

        return Objects.equals(this.accountNumber, other.accountNumber);
             
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

}
