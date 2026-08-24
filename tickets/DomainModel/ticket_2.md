# EHB Ticket 002 — Add Withdrawals to Account

## Objective

Extend the `Account` class so that money can be withdrawn.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise business-rule validation, `BigDecimal` comparison, and encapsulation.

## Requirements

Add a `withdraw` method to the existing `Account` class:

```java
account.withdraw(amount);
```

The amount must be supplied as a `BigDecimal`.

A successful withdrawal reduces the account balance.

## Withdrawal Behaviour

For example:

```text
Starting balance: 200.00
Withdrawal:        75.00
Resulting balance: 125.00
```

## Business Rules

1. A withdrawal amount must be positive and greater than zero.
2. A withdrawal amount must not exceed the current balance.
3. A zero withdrawal must be rejected.
4. A negative withdrawal must be rejected.
5. External code must not be able to arbitrarily modify the account balance.
6. Balance reduction must happen inside `Account`.

## Package

The `Account` class lives in:

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

## Things to Think About

While implementing the ticket, consider:

* What exception should `withdraw()` throw for an invalid amount?
* What exception should `withdraw()` throw when there are insufficient funds?
* How should two `BigDecimal` values be compared?
* Should the same validation logic used in `deposit()` be reused or duplicated?
* What is the difference between rejecting an invalid amount and rejecting due to insufficient funds?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement the following as part of this ticket:

* `Transaction` recording
* `Business`
* `Payment`
* Spring Boot
* REST controllers
* JPA
* PostgreSQL
* Kafka
* AWS

These will be introduced in later tickets.

## Testing

JUnit tests are not required for this ticket.

You may create a small `main()` method or other temporary mechanism to exercise the behaviour manually.

Demonstrate that:

* a valid withdrawal reduces the balance
* a withdrawal exceeding the balance is rejected
* a zero or negative withdrawal is rejected

## Definition of Done

The ticket is complete when:

* `withdraw(BigDecimal)` correctly reduces the balance for a valid amount.
* Zero and negative withdrawals are rejected.
* A withdrawal greater than the current balance is rejected.
* The balance cannot be directly modified by code outside `Account`.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the code you have written and can explain the design decisions.
