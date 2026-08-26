package com.ehb.banking;

import com.ehb.banking.exceptions.NonPositiveAmountException;

/**
 * Validates that a payment's amount is strictly positive.
 * Stateless and safely reusable across multiple validations.
 */
public class PositiveAmountValidator implements PaymentValidator {

    @Override
    public void validate(Payment payment, Account account) {
        if (payment.getPaymentAmount() == null
                || payment.getPaymentAmount().signum() <= 0) {
            throw new NonPositiveAmountException(
                    "Payment amount must be positive, but was: " + payment.getPaymentAmount());
        }
    }
}
