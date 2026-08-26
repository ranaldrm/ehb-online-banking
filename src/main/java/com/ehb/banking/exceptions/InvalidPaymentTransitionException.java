package com.ehb.banking.exceptions;

public class InvalidPaymentTransitionException extends BankingException {

    public InvalidPaymentTransitionException(String message) {
        super(message);
    }

    public InvalidPaymentTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}