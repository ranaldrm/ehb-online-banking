# EHB Ticket 014 — Test Account Business Rules

## Objective

Write unit tests covering the behaviour already implemented in `Account`.

At this stage, the application is **plain Java 21** with JUnit 5 (introduced in Ticket 013). Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise unit testing, assertions, `assertThrows`, test fixture arrangement, and behavioural testing.

## Requirements

Write tests covering the following behaviours:

### Balance Behaviour

* A newly created account starts with a zero balance.

### Deposit Behaviour

* A valid deposit increases the balance by the deposited amount.
* A deposit of zero is rejected.
* A negative deposit is rejected.

### Withdrawal Behaviour

* A valid withdrawal decreases the balance by the withdrawn amount.
* A withdrawal greater than the current balance is rejected.
* A withdrawal of zero is rejected.
* A negative withdrawal is rejected.

### Invariant Behaviour

* A failed deposit does not change the balance.
* A failed withdrawal does not change the balance.

### Transaction Recording

* A successful deposit creates an incoming transaction in the account's history.
* A successful withdrawal creates an outgoing transaction in the account's history.
* A failed operation does not create a transaction.

## Test Structure

Place tests in:

```text
src/test/java/com/ehb/banking/AccountTest.java
```

A typical test method structure:

```java
@Test
void validDepositIncreasesBalance() {
    // Arrange
    Account account = new Account("12345678", Currency.GBP);

    // Act
    account.deposit(new BigDecimal("100.00"));

    // Assert
    assertEquals(new BigDecimal("100.00"), account.getBalance());
}
```

Use `assertThrows` to verify that exceptions are thrown:

```java
@Test
void zeroDepositIsRejected() {
    Account account = new Account("12345678", Currency.GBP);

    assertThrows(SomeException.class, () -> account.deposit(BigDecimal.ZERO));
}
```

Replace `SomeException.class` with the actual custom exception type introduced in Ticket 003.

## Package

```text
src/test/java/com/ehb/banking/AccountTest.java
```

## Things to Think About

While writing the tests, consider:

* Should each test be independent? (Yes — avoid sharing mutable state between tests.)
* Should you use `@BeforeEach` to create a fresh `Account` for each test?
* How do you compare `BigDecimal` values safely in assertions?
* Are you testing behaviour or implementation details?
* What is the difference between testing that an exception is thrown and testing what the message says?

## Out of Scope

Do **not** write tests for `Business`, `Transaction` queries, or `Payment` in this ticket.

Do **not** implement:

* Spring Boot
* Spring Test
* JPA
* PostgreSQL
* Kafka
* AWS

## Definition of Done

The ticket is complete when:

* All test cases listed above are implemented and pass.
* Running `mvn test` succeeds with no failures.
* Tests are independent and do not rely on each other.
* You can explain what each test is verifying and why.
