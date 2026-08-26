package com.ehb.banking.exceptions;

public class DuplicateAccountNumberException extends BankingException{

    public DuplicateAccountNumberException(String message){

        super(message);
    }

        public DuplicateAccountNumberException(String message, Throwable cause){

        super(message, cause);
    }


}