# EHB Ticket 005 — Record Account Transactions

## Objective

Connect `Account` and `Transaction` so that every successful money movement is recorded.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise object relationships, encapsulation of collections, and ensuring that domain invariants are upheld.

## Requirements

* An `Account` maintains an internal transaction history.
* A successful `deposit()` creates and stores an incoming transaction.
* A successful `withdraw()` creates and stores an outgoing transaction.
* Failed operations (e.g. invalid amount, insufficient funds) must **not** create a transaction.
* External code must not be able to modify the internal transaction list arbitrarily.

Provide a way for external code to retrieve the account's transactions.

## Transaction Recording

When a deposit succeeds:

```text
A transaction of type INCOMING is created and added to the account's history.
```

When a withdrawal succeeds:

```text
A transaction of type OUTGOING is created and added to the account's history.
```

When an operation fails:

```text
No transaction is created. The history is unchanged.
```

## Encapsulation of the Collection

The internal list of transactions must be protected. Consider:

* Should `getTransactions()` return the internal list directly?
* Should it return an unmodifiable view?
* Should it return a defensive copy?

External code must not be able to call `add()`, `remove()`, or `clear()` on the returned collection and affect the internal state of the account.

## Package

All classes remain in:

```text
src/main/java/com/ehb/banking/
```

The Java package is:

```java
package com.ehb.banking;
```

## Files

For this ticket, modify:

```text
src/main/java/com/ehb/banking/Account.java
```

`Transaction` and `TransactionType` were introduced in Ticket 004.

## Things to Think About

While implementing the ticket, consider:

* Where should the `Transaction` be created — inside `Account`, or passed in from outside?
* Should the transaction list be a `List`, `Set`, or `Deque`?
* What is the difference between `Collections.unmodifiableList()` and returning a new `ArrayList` copy?
* Is the order of transactions significant?
* Should the account number or currency be recorded on the transaction?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement the following as part of this ticket:

* query methods such as `getOutgoingTransactions()` (Ticket 006)
* `Business`
* `Payment`
* Spring Boot
* REST controllers
* JPA
* PostgreSQL
* Kafka
* AWS

## Testing

JUnit tests are not required for this ticket.

Verify manually that:

* a successful deposit results in one transaction in the history
* a successful withdrawal results in one transaction in the history
* a failed operation leaves the history unchanged
* the returned collection cannot be modified externally

## Definition of Done

The ticket is complete when:

* Successful deposits create an incoming transaction in the account's history.
* Successful withdrawals create an outgoing transaction in the account's history.
* Failed operations do not create transactions.
* The transaction list cannot be arbitrarily modified by external code.
* `getTransactions()` returns the account's transaction history.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the design choices made and can explain them.
