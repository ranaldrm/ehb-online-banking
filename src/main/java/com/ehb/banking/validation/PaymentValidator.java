package com.ehb.banking.validation;

import com.ehb.banking.domain.Account;
import com.ehb.banking.domain.Payment;

public interface PaymentValidator {
    /**
     * Validates a payment against a specific rule.
     *
     * @param payment the payment to validate
     * @param account the source account associated with the payment
     * @throws com.ehb.banking.exceptions.BankingException if the payment fails validation
     */
    void validate(Payment payment, Account account);
}
