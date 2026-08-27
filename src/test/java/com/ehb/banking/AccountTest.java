package com.ehb.banking;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ehb.banking.exceptions.ExceedsBalanceException;
import com.ehb.banking.exceptions.NonPositiveAmountException;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("12345678", Currency.GBP);
    }

    // --- Balance Behaviour ---

    @Test
    void newAccountHasZeroBalance() {
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    // --- Deposit Behaviour ---

    @Test
    void validDepositIncreasesBalance() {
        // Act
        account.deposit(new BigDecimal("100.00"));

        // Assert
        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }

    @Test
    void zeroDepositIsRejected() {
        assertThrows(NonPositiveAmountException.class, () -> account.deposit(BigDecimal.ZERO));
    }

    @Test
    void negativeDepositIsRejected() {
        assertThrows(NonPositiveAmountException.class, () -> account.deposit(new BigDecimal("-0.04")));
    }

    // --- Withdrawal Behaviour ---

    @Test
    void validWithdrawalDecreasesBalance() {
        // Arrange
        account.deposit(new BigDecimal("100.00"));

        // Act
        account.withdraw(new BigDecimal("40.00"));

        // Assert
        assertEquals(new BigDecimal("60.00"), account.getBalance());
    }

    @Test
    void withdrawalExceedingBalanceIsRejected() {
        // Arrange
        account.deposit(new BigDecimal("50.00"));

        // Act & Assert
        assertThrows(ExceedsBalanceException.class, () -> account.withdraw(new BigDecimal("100.00")));
    }

    @Test
    void zeroWithdrawalIsRejected() {
        assertThrows(NonPositiveAmountException.class, () -> account.withdraw(BigDecimal.ZERO));
    }

    @Test
    void negativeWithdrawalIsRejected() {
        assertThrows(NonPositiveAmountException.class, () -> account.withdraw(new BigDecimal("-10.00")));
    }

    // --- Invariant Behaviour ---

    @Test
    void failedDepositDoesNotChangeBalance() {
        // Act — attempt an invalid deposit
        assertThrows(NonPositiveAmountException.class, () -> account.deposit(BigDecimal.ZERO));

        // Assert — balance unchanged
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void failedWithdrawalDoesNotChangeBalance() {
        // Arrange
        account.deposit(new BigDecimal("50.00"));

        // Act — attempt withdrawal exceeding balance
        assertThrows(ExceedsBalanceException.class, () -> account.withdraw(new BigDecimal("200.00")));

        // Assert — balance unchanged
        assertEquals(new BigDecimal("50.00"), account.getBalance());
    }

    // --- Transaction Recording ---

    @Test
    void successfulDepositCreatesIncomingTransaction() {
        // Act
        account.deposit(new BigDecimal("100.00"));

        // Assert
        assertEquals(1, account.getTransactions().size());
        assertEquals(TransactionType.INCOMING, account.getTransactions().get(0).transactionType());
    }

    @Test
    void successfulWithdrawalCreatesOutgoingTransaction() {
        // Arrange
        account.deposit(new BigDecimal("100.00"));

        // Act
        account.withdraw(new BigDecimal("40.00"));

        // Assert — deposit + withdrawal = 2 transactions, last one is outgoing
        assertEquals(2, account.getTransactions().size());
        assertEquals(TransactionType.OUTGOING, account.getTransactions().get(1).transactionType());
    }

    @Test
    void failedOperationDoesNotCreateTransaction() {
        // Act — attempt an invalid deposit
        assertThrows(NonPositiveAmountException.class, () -> account.deposit(BigDecimal.ZERO));

        // Assert — no transaction recorded
        assertTrue(account.getTransactions().isEmpty());
    }
}
