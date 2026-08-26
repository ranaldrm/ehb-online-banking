package com.ehb.banking;

import java.util.List;

/**
 * Runs a sequence of {@link PaymentValidator}s in order.
 * Fails fast: stops at the first validator that throws an exception.
 * This allows callers to compose any set of rules without modifying existing validators.
 */
public class CompositePaymentValidator implements PaymentValidator {

    private final List<PaymentValidator> validators;

    public CompositePaymentValidator(List<PaymentValidator> validators) {
        this.validators = List.copyOf(validators);
    }

    @Override
    public void validate(Payment payment, Account account) {
        for (PaymentValidator validator : validators) {
            validator.validate(payment, account);
        }
    }
}
