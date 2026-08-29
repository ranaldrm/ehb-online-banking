# EHB Ticket 007 — Create the Business Domain Model

## Objective

Introduce the fictional organisation that owns EHB accounts.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise object relationships, encapsulation of collections, `Optional`, and object ownership.

## Requirements

Create a `Business` class containing suitable identifying information.

A business should be able to own one or more accounts.

Provide behaviour for:

* adding an account to the business
* retrieving all accounts belonging to the business
* locating a single account by an appropriate identifier (e.g. account number)

Prevent external code from arbitrarily modifying the internal account collection.

## Business Identity

A business should have at minimum:

* a name or identifier that distinguishes it from other businesses

Decide what additional fields, if any, are appropriate at this stage.

## Account Lookup

When locating an account by identifier, consider that the account may not exist. Use `Optional` to represent this possibility:

```java
Optional<Account> findAccount(String accountNumber);
```

## Encapsulation of the Collection

The internal list or collection of accounts must be protected. Consider:

* Should `getAccounts()` return the internal collection directly?
* Should it return an unmodifiable view?
* Should it return a defensive copy?

External code must not be able to add or remove accounts from the collection by means other than the provided `addAccount()` method.

## Package

Place `Business` in the same package as the other domain classes:

```text
src/main/java/com/ehb/banking/
```

The Java package is:

```java
package com.ehb.banking;
```

## Files

For this ticket, create:

```text
src/main/java/com/ehb/banking/Business.java
```

## Things to Think About

While implementing the ticket, consider:

* What is the identifier for a business — a name, a registration number, a UUID?
* Should `Business` hold accounts in a `List`, `Set`, or `Map`?
* A `Map<String, Account>` (keyed by account number) could simplify lookup — is it a good trade-off?
* Should adding a duplicate account number be rejected?
* Should `findAccount()` throw an exception or return `Optional.empty()` when not found?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement the following as part of this ticket:

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

* a `Business` can be created
* accounts can be added
* all accounts can be retrieved
* a specific account can be found by its identifier
* the returned collection cannot be modified externally

## Definition of Done

The ticket is complete when:

* `Business` exists with appropriate identifying information.
* Accounts can be added to a business.
* All accounts can be retrieved without exposing the internal collection.
* An account can be located by identifier, returning `Optional<Account>`.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the design choices made and can explain them.
