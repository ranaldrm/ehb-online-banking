package com.ehb.banking.exceptions;

public class BusinessNotFoundException extends BankingException{

    public BusinessNotFoundException(String message){

        super(message);
    }

        public BusinessNotFoundException(String message, Throwable cause){

        super(message, cause);
    }


}