package com.ehb.banking;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.exceptions.InvalidPaymentTransitionException;
import com.ehb.banking.exceptions.NonPositiveAmountException;

class PaymentTest {

    // -----------------------------------------------------------------------
    // Helper: creates an Account wired with the full composite validator
    // -----------------------------------------------------------------------
    private Account accountWithValidator(String number, Currency currency) {
        PaymentValidator validator = new CompositePaymentValidator(
                List.of(new PositiveAmountValidator(), new SufficientFundsValidator()));
        return new Account(number, currency, validator);
    }

    // -----------------------------------------------------------------------
    // Payment Creation
    // -----------------------------------------------------------------------

    @Test
    void newPaymentHasCreatedStatus() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");

        assertEquals(PaymentStatus.CREATED, payment.getPaymentStatus());
    }

    // -----------------------------------------------------------------------
    // Valid State Transitions
    // -----------------------------------------------------------------------

    @Test
    void createdPaymentCanBeValidated() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");

        payment.validate();

        assertEquals(PaymentStatus.VALIDATED, payment.getPaymentStatus());
    }

    @Test
    void validatedPaymentCanBeApproved() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.validate();

        payment.approve();

        assertEquals(PaymentStatus.APPROVED, payment.getPaymentStatus());
    }

    @Test
    void approvedPaymentCanBeCompleted() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.validate();
        payment.approve();

        payment.complete();

        assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());
    }

    @Test
    void createdPaymentCanBeRejected() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");

        payment.reject();

        assertEquals(PaymentStatus.REJECTED, payment.getPaymentStatus());
    }

    @Test
    void validatedPaymentCanBeRejected() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.validate();

        payment.reject();

        assertEquals(PaymentStatus.REJECTED, payment.getPaymentStatus());
    }

    @Test
    void approvedPaymentCanBeRejected() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.validate();
        payment.approve();

        payment.reject();

        assertEquals(PaymentStatus.REJECTED, payment.getPaymentStatus());
    }

    // -----------------------------------------------------------------------
    // Invalid State Transitions
    // -----------------------------------------------------------------------

    @Test
    void approvingCreatedPaymentThrows() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");

        assertThrows(InvalidPaymentTransitionException.class, payment::approve);
    }

    @Test
    void completingValidatedPaymentThrows() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.validate();

        assertThrows(InvalidPaymentTransitionException.class, payment::complete);
    }

    @Test
    void completingRejectedPaymentThrows() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.reject();

        assertThrows(InvalidPaymentTransitionException.class, payment::complete);
    }

    @Test
    void transitioningCompletedPaymentToAnyStatusThrows() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.validate();
        payment.approve();
        payment.complete();

        assertThrows(InvalidPaymentTransitionException.class, payment::validate);
        assertThrows(InvalidPaymentTransitionException.class, payment::approve);
        assertThrows(InvalidPaymentTransitionException.class, payment::reject);
    }

    @Test
    void transitioningRejectedPaymentToAnyStatusThrows() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.reject();

        assertThrows(InvalidPaymentTransitionException.class, payment::validate);
        assertThrows(InvalidPaymentTransitionException.class, payment::approve);
        assertThrows(InvalidPaymentTransitionException.class, payment::reject);
    }

    // -----------------------------------------------------------------------
    // Validation Failure (exercised via Account.processOutgoingPayment)
    // -----------------------------------------------------------------------

    @Test
    void paymentWithZeroAmountCannotBeCreated() {
        // The Payment constructor itself guards against non-positive amounts
        assertThrows(NonPositiveAmountException.class,
                () -> new Payment(BigDecimal.ZERO, "11111111", "22222222"));
    }

    @Test
    void paymentWithNegativeAmountCannotBeCreated() {
        assertThrows(NonPositiveAmountException.class,
                () -> new Payment(new BigDecimal("-50.00"), "11111111", "22222222"));
    }

    @Test
    void insufficientFundsFailsValidationDuringProcessing() {
        // Arrange
        Account source = accountWithValidator("11111111", Currency.GBP);
        Account target = accountWithValidator("22222222", Currency.GBP);
        source.deposit(new BigDecimal("100.00"));

        // Act & Assert — trying to pay more than the balance triggers SufficientFundsValidator
        assertThrows(ExceedsBalanceException.class,
                () -> source.processOutgoingPayment(new BigDecimal("200.00"), target));
    }

    // -----------------------------------------------------------------------
    // Successful Completion via Account
    // -----------------------------------------------------------------------

    @Test
    void completingApprovedPaymentDeductsAmountFromSourceAccount() {
        // Arrange
        Account source = accountWithValidator("11111111", Currency.GBP);
        Account target = accountWithValidator("22222222", Currency.GBP);
        source.deposit(new BigDecimal("500.00"));

        // Act
        source.processOutgoingPayment(new BigDecimal("200.00"), target);

        // Assert — 500 - 200 = 300
        assertEquals(0, source.getBalance().compareTo(new BigDecimal("300.00")));
    }

    @Test
    void completingApprovedPaymentRecordsOutgoingTransactionOnSourceAccount() {
        // Arrange
        Account source = accountWithValidator("11111111", Currency.GBP);
        Account target = accountWithValidator("22222222", Currency.GBP);
        source.deposit(new BigDecimal("500.00"));

        // Act
        source.processOutgoingPayment(new BigDecimal("200.00"), target);

        // Assert — 1 deposit + 1 outgoing payment = 2 transactions; the last is OUTGOING
        assertEquals(2, source.getTransactions().size());
        assertEquals(TransactionType.OUTGOING,
                source.getTransactions().get(1).transactionType());
    }

    // -----------------------------------------------------------------------
    // Prevention of Duplicate Completion
    // -----------------------------------------------------------------------

    @Test
    void callingCompleteOnAlreadyCompletedPaymentThrows() {
        Payment payment = new Payment(new BigDecimal("100.00"), "11111111", "22222222");
        payment.validate();
        payment.approve();
        payment.complete();

        assertThrows(InvalidPaymentTransitionException.class, payment::complete);
    }

    // -----------------------------------------------------------------------
    // Insufficient Funds on Completion — account state must be unchanged
    // -----------------------------------------------------------------------

    @Test
    void insufficientFundsOnCompletionLeavesBalanceUnchanged() {
        // Arrange — source has exactly 100; we request 200
        Account source = accountWithValidator("11111111", Currency.GBP);
        Account target = accountWithValidator("22222222", Currency.GBP);
        source.deposit(new BigDecimal("100.00"));

        // Act — this must fail
        assertThrows(ExceedsBalanceException.class,
                () -> source.processOutgoingPayment(new BigDecimal("200.00"), target));

        // Assert — balance is still 100
        assertEquals(0, source.getBalance().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void insufficientFundsOnCompletionDoesNotRecordTransaction() {
        // Arrange
        Account source = accountWithValidator("11111111", Currency.GBP);
        Account target = accountWithValidator("22222222", Currency.GBP);
        source.deposit(new BigDecimal("100.00"));

        // Act — this must fail
        assertThrows(ExceedsBalanceException.class,
                () -> source.processOutgoingPayment(new BigDecimal("200.00"), target));

        // Assert — only the initial deposit transaction exists; no outgoing was recorded
        assertEquals(1, source.getTransactions().size());
        assertEquals(TransactionType.INCOMING, source.getTransactions().get(0).transactionType());
    }
}
