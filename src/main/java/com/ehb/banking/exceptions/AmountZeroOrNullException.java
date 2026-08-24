package com.ehb.banking.exceptions;

public class AmountZeroOrNullException extends BankingException{

    public AmountZeroOrNullException(String message){

        super(message);
    }
}