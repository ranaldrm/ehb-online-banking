package com.ehb.banking;

import com.ehb.banking.exceptions.InvalidPaymentException;

/**
 * Validates that the payment's declared currency matches the source account's currency.
 * Stateless and safely reusable across multiple validations.
 */
public class CurrencyMatchValidator implements PaymentValidator {

    private final Currency expectedCurrency;

    /**
     * @param expectedCurrency the currency the payment must be denominated in
     */
    public CurrencyMatchValidator(Currency expectedCurrency) {
        this.expectedCurrency = expectedCurrency;
    }

    @Override
    public void validate(Payment payment, Account account) {
        if (!account.getCurrency().equals(expectedCurrency)) {
            throw new InvalidPaymentException(
                    "Payment currency " + expectedCurrency
                    + " does not match account currency " + account.getCurrency()
                    + " on account " + account.getAccountNumber());
        }
    }
}
