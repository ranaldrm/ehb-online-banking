package com.ehb.banking;

import java.math.BigDecimal;
import java.util.List;

public class BankingSystem {
    public static void main (String[] args){

        // Build a composite validator with all payment rules
        PaymentValidator paymentValidator = new CompositePaymentValidator(List.of(
            new PositiveAmountValidator(),
            new CurrencyMatchValidator(Currency.GBP),
            new SufficientFundsValidator()
        ));

        Account account1 = new Account("123", Currency.GBP, paymentValidator);

        System.out.println(account1);

        System.out.println("transactions" + account1.getTransactions().toString());

        account1.deposit(new BigDecimal("50.00"));

        System.out.println(account1);

        System.out.println("transactions" + account1.getTransactions().toString());

        Account account2 = new Account("124", Currency.USD, new CompositePaymentValidator(List.of(
            new PositiveAmountValidator(),
            new CurrencyMatchValidator(Currency.USD),
            new SufficientFundsValidator()
        )));

        account2.deposit(new BigDecimal("40.00"));

        account2.withdraw(new BigDecimal("2.00"));

        System.out.println(account2);

        System.out.println("transactions" + account2.getTransactions().toString());

        System.out.println("total incoming transactions " + account2.getIncomingTransactions().toString());

        System.out.println("total outgoing transaction sum " + account2.getTotalOutgoingPayments().toString());

        // Example: create a payment from account1 to account2
        Payment payment = new Payment(new BigDecimal("10.00"), account1.getAccountNumber(), account2.getAccountNumber());
        System.out.println("Payment from " + payment.getSourceAccountNumber() + " to " + payment.getTargetAccountNumber());


    }



}