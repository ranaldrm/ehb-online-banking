# EHB Ticket 015 — Test Business and Transaction Queries

## Objective

Write unit tests covering the relationships between `Business` and `Account`, and the transaction query behaviour on `Account`.

At this stage, the application is **plain Java 21** with JUnit 5 (introduced in Ticket 013). Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise collection testing, Stream-based query verification, `Optional` assertions, and equality checks.

## Requirements

### Business and Account Relationship

* A business starts with no accounts.
* An account can be added to a business.
* Multiple accounts can be added to a business.
* A specific account can be found by its identifier.
* Looking up an account that does not exist returns `Optional.empty()`.

### Collection Encapsulation

* The collection returned by `getAccounts()` cannot be modified to affect the internal state of the business.

For example, calling `add()` or `remove()` on the returned collection should either:

* throw an `UnsupportedOperationException`, or
* not affect the internal account list.

### Transaction History

* A new account has an empty transaction history.
* After a deposit, the transaction history contains one incoming transaction.
* After a withdrawal, the transaction history contains one outgoing transaction.

### Transaction Query Methods

* `getOutgoingTransactions()` returns only outgoing transactions.
* `getOutgoingTransactions()` returns an empty list when there are no outgoing transactions.
* `getTotalOutgoingPayments()` returns the correct total for one or more outgoing transactions.
* `getTotalOutgoingPayments()` returns `BigDecimal.ZERO` when there are no outgoing transactions.

## Test Structure

Place tests in:

```text
src/test/java/com/ehb/banking/BusinessTest.java
src/test/java/com/ehb/banking/TransactionQueryTest.java
```

Or combine them in a single file if you prefer — name it appropriately.

A typical `Optional` assertion:

```java
@Test
void findAccountReturnsEmptyWhenNotFound() {
    Business business = new Business("EHB Ltd");

    Optional<Account> result = business.findAccount("nonexistent");

    assertTrue(result.isEmpty());
}
```

## Things to Think About

While writing the tests, consider:

* How do you assert that a collection is unmodifiable?
* How do you assert on the contents of a `List` — `assertEquals`, `assertIterableEquals`, or `assertTrue(list.contains(...))`?
* Should transaction query tests set up deposits and withdrawals to generate transactions?
* Is `BigDecimal` comparison in assertions safe with `assertEquals`?
* Should each test method set up its own `Business` and `Account` objects?

## Out of Scope

Do **not** write `Payment` lifecycle tests in this ticket — those are Ticket 016.

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
* Tests are independent and clearly named.
* You can explain what each test is verifying and why.
