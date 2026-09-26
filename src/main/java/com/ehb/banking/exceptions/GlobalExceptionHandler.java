package com.ehb.banking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ehb.banking.dto.ErrorResponse;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity <ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BusinessNotFoundException.class)
    public ResponseEntity <ErrorResponse> handleBusinessNotFound(BusinessNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NonPositiveAmountException.class)
    public ResponseEntity<ErrorResponse> handleNonPositiveAmount(NonPositiveAmountException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ExceedsBalanceException.class)
    public ResponseEntity<ErrorResponse> handleExceedsBalance(ExceedsBalanceException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicateAccountNumberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateAccountNumber(DuplicateAccountNumberException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPayment(InvalidPaymentException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidPaymentTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPaymentTransition(InvalidPaymentTransitionException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}