# EHB Ticket 001 — Create the Account Domain Class

## Objective

Create the first domain class for the Edinburgh Hanoverian Bank (EHB) Online Banking project.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise basic domain modelling, encapsulation, enums, and `BigDecimal`.

## Requirements

Create an `Account` class representing an EHB bank account.

An account must have:

* an account number
* a currency
* a balance

Create a `Currency` enum initially containing:

```java
GBP,
EUR,
USD
```

Use `BigDecimal` to represent monetary values.

## Account Creation

An account must be created with:

* an account number
* a currency

Its initial balance should be zero.

For example, the following kind of usage should be possible:

```java
Account account = new Account("12345678", Currency.GBP);
```

## Deposit Behaviour

The `Account` class must provide a method allowing money to be deposited:

```java
account.deposit(amount);
```

The amount must be supplied as a `BigDecimal`.

A successful deposit increases the account balance.

For example:

```text
Starting balance: 100.00
Deposit:           50.00
Resulting balance: 150.00
```

## Business Rules

1. An account must have an account number and currency.
2. The initial account balance is zero.
3. Deposits must use `BigDecimal`.
4. A positive deposit increases the balance.
5. A deposit of zero must be rejected.
6. A negative deposit must be rejected.
7. External code must not be able to arbitrarily modify the account balance.

## Package

Place the classes in:

```text
src/main/java/com/ehb/banking/
```

The Java package is therefore:

```java
package com.ehb.banking;
```

## Files

For this ticket, create:

```text
src/main/java/com/ehb/banking/Account.java
src/main/java/com/ehb/banking/Currency.java
```

## Things to Think About

While implementing the ticket, consider:

* Should `balance` have a setter?
* Should `accountNumber` be changeable after an `Account` has been created?
* Should `currency` be changeable?
* What exception should `deposit()` throw when given an invalid amount?
* How should a `BigDecimal` be compared with zero?
* Would `double` be appropriate anywhere in this class?
* What access modifiers should the fields and methods have?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement the following as part of this ticket:

* `Business`
* `Transaction`
* withdrawals
* payments
* transaction history
* Spring Boot
* REST controllers
* JPA
* PostgreSQL
* Kafka
* AWS

These will be introduced in later tickets.

## Testing

JUnit tests are not required for Ticket 001.

You may create a small `main()` method or other temporary mechanism to exercise the behaviour manually.

You should demonstrate that a valid deposit works and that an invalid deposit such as:

```java
account.deposit(new BigDecimal("-50.00"));
```

is rejected.

## Definition of Done

The ticket is complete when:

* `Currency` contains `GBP`, `EUR`, and `USD`.
* `Account` can be constructed with an account number and currency.
* A newly created account has a zero balance.
* `deposit(BigDecimal)` correctly increases the balance.
* zero and negative deposits are rejected.
* the balance cannot be directly changed by code outside `Account`.
* the project successfully compiles with:

```powershell
mvn compile
```

* you understand the code you have written and can explain the design decisions.
