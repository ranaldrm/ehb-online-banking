package com.ehb.banking.exceptions;

public class ExceedsBalanceException extends BankingException{

    public ExceedsBalanceException(String message){

        super(message);
    }

        public ExceedsBalanceException(String message, Throwable cause){

        super(message, cause);
    }


}