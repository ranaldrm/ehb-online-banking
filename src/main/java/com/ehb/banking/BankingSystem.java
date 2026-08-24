package com.ehb.banking;

import java.math.BigDecimal;

public class BankingSystem {
    public static void main (String[] args){
        Account account1 = new Account ("123", Currency.GBP);

        System.out.println(account1);

        account1.deposit(new BigDecimal("50.00"));

        System.out.println(account1);

        Account account2 = new Account ("124", Currency.USD);

        account2.deposit(new BigDecimal("40.00"));

        account2.withdraw(new BigDecimal("2.00"));

        System.out.println(account2);


    }



}