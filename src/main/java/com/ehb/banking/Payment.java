package com.ehb.banking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ehb.banking.exceptions.InvalidPaymentException;
import com.ehb.banking.exceptions.InvalidPaymentTransitionException;
import com.ehb.banking.exceptions.NonPositiveAmountException;

public class Payment {

    private PaymentStatus paymentStatus;
    private final String paymentID;
    private final BigDecimal paymentAmount;
    private final LocalDateTime paymentTime;
    private final String sourceAccountNumber;
    private final String targetAccountNumber;



    public Payment (BigDecimal paymentAmount, String sourceAccountNumber, String targetAccountNumber) {
        if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new NonPositiveAmountException("paymentAmount must be greater than 0 when creating payment");
        }
        if (sourceAccountNumber == null || sourceAccountNumber.isBlank()) {
            throw new InvalidPaymentException("sourceAccountNumber is required to create a Payment");
        }
        if (targetAccountNumber == null || targetAccountNumber.isBlank()) {
            throw new InvalidPaymentException("targetAccountNumber is required to create a Payment");
        }
        this.paymentStatus = PaymentStatus.CREATED;
        this.paymentID =  UUID.randomUUID().toString();
        this.paymentAmount = paymentAmount;
        this.paymentTime = LocalDateTime.now();
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }


    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public String getSourceAccountNumber() {
        return this.sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return this.targetAccountNumber;
    }

    public void validate() {
        switch (this.paymentStatus) {
            case CREATED -> this.paymentStatus = PaymentStatus.VALIDATED;
            case VALIDATED -> throw new InvalidPaymentTransitionException ("Cannot validate. Payment status is " + this.paymentStatus);
            case APPROVED -> throw new InvalidPaymentTransitionException ("Cannot validate. Payment status is " + this.paymentStatus);
            case COMPLETED -> throw new InvalidPaymentTransitionException ("Cannot validate. Payment status is " + this.paymentStatus);
            case REJECTED -> throw new InvalidPaymentTransitionException ("Cannot validate. Payment status is " + this.paymentStatus);
        }
    }
    public void approve() {
        switch (this.paymentStatus) {
            case VALIDATED  -> this.paymentStatus = PaymentStatus.APPROVED;
            case CREATED    -> throw new InvalidPaymentTransitionException("Cannot approve. Payment status is " + this.paymentStatus);
            case APPROVED   -> throw new InvalidPaymentTransitionException("Cannot approve. Payment status is " + this.paymentStatus);
            case COMPLETED  -> throw new InvalidPaymentTransitionException("Cannot approve. Payment status is " + this.paymentStatus);
            case REJECTED   -> throw new InvalidPaymentTransitionException("Cannot approve. Payment status is " + this.paymentStatus);
        }
    }

    public void complete() {
        switch (this.paymentStatus) {
            case APPROVED   -> this.paymentStatus = PaymentStatus.COMPLETED;
            case CREATED    -> throw new InvalidPaymentTransitionException("Cannot complete. Payment status is " + this.paymentStatus);
            case VALIDATED  -> throw new InvalidPaymentTransitionException("Cannot complete. Payment status is " + this.paymentStatus);
            case COMPLETED  -> throw new InvalidPaymentTransitionException("Cannot complete. Payment status is " + this.paymentStatus);
            case REJECTED   -> throw new InvalidPaymentTransitionException("Cannot complete. Payment status is " + this.paymentStatus);
        }
    }

    public void reject() {
        switch (this.paymentStatus) {
            case CREATED    -> this.paymentStatus = PaymentStatus.REJECTED;
            case VALIDATED  -> this.paymentStatus = PaymentStatus.REJECTED;
            case APPROVED   -> this.paymentStatus = PaymentStatus.REJECTED;
            case COMPLETED  -> throw new InvalidPaymentTransitionException("Cannot reject. Payment status is " + this.paymentStatus);
            case REJECTED   -> throw new InvalidPaymentTransitionException("Cannot reject. Payment status is " + this.paymentStatus);
        }
    }

}