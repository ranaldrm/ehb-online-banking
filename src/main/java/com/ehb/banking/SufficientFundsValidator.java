package com.ehb.banking;

import com.ehb.banking.exceptions.ExceedsBalanceException;

/**
 * Validates that the source account holds enough funds to cover the payment amount.
 * Stateless and safely reusable across multiple validations.
 */
public class SufficientFundsValidator implements PaymentValidator {

    @Override
    public void validate(Payment payment, Account account) {
        if (payment.getPaymentAmount().compareTo(account.getBalance()) > 0) {
            throw new ExceedsBalanceException(
                    "Payment amount of " + payment.getPaymentAmount()
                    + " exceeds available balance of " + account.getBalance()
                    + " on account " + account.getAccountNumber());
        }
    }
}
