package com.ehb.banking.exceptions;

public class NonPositiveAmountException extends BankingException{

    public NonPositiveAmountException(String message){

        super(message);
    }

    public NonPositiveAmountException(String message, Throwable cause){

        super(message, cause);
    }
}