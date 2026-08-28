package com.ehb.banking;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessTest {

    private Business business;
    private Account account;

    @BeforeEach
    void setUp() {
        business = new Business("The Bada Bing", "password1");
        account = new Account("1111", Currency.GBP);
    }

    // --- Business and Account Relationship ---

    @Test
    void businessStartsWithNoAccounts() {
        assertTrue(business.getAllAccounts().isEmpty());
    }

    @Test
    void addAccountToBusiness() {
        business.addAccount(account);
        assertEquals(1, business.getAllAccounts().size());
    }

    @Test
    void addMultipleAccountsToBusiness() {
        Account account2 = new Account("2222", Currency.GBP);
        business.addAccount(account);
        business.addAccount(account2);
        assertEquals(2, business.getAllAccounts().size());
    }

    @Test
    void lookUpAccountByNumber() {
        business.addAccount(account);
        Account found = business.getAccount("1111");
        assertEquals("1111", found.getAccountNumber());
    }

    @Test
    void findAccountReturnsEmptyWhenNotFound() {
        Optional<Account> result = business.findAccount("nonexistent");
        assertTrue(result.isEmpty());
    }

    // --- Collection Encapsulation ---

    @Test
    void getAllAccountsReturnsUnmodifiableMap() {
        // Arrange
        business.addAccount(account);
        Map<String, Account> snapshot = business.getAllAccounts();

        // Act & Assert — attempting to mutate the returned map must throw
        assertThrows(UnsupportedOperationException.class,
                () -> snapshot.put("9999", new Account("9999", Currency.GBP)));

        // The internal state must be unaffected
        assertEquals(1, business.getAllAccounts().size());
    }
}
