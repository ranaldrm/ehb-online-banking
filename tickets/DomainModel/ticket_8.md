# EHB Ticket 008 — Define Equality and Identity Rules

## Objective

Review the domain model and decide what makes domain objects equal.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise `equals()`, `hashCode()`, object identity versus value equality, and the implications for collections such as `Set` and `Map`.

## Requirements

Consider and implement appropriate `equals()` and `hashCode()` methods for the relevant domain classes:

* `Account`
* `Business`
* `Transaction`

For each class, decide whether equality should be based on:

* a single unique identifier (identity equality)
* a combination of meaningful fields (value equality)
* reference equality (the default behaviour — is this acceptable?)

## Equality Decisions

Think through each class:

### Account

Two accounts are equal if… (decide based on domain reasoning).

### Business

Two businesses are equal if… (decide based on domain reasoning).

### Transaction

Two transactions are equal if… (decide based on domain reasoning).

## Consistency Rule

Java requires that `equals()` and `hashCode()` are consistent:

* If `a.equals(b)` is `true`, then `a.hashCode()` must equal `b.hashCode()`.
* If objects are used in a `Set` or as `Map` keys, both must be correctly implemented.

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

For this ticket, modify relevant files such as:

```text
src/main/java/com/ehb/banking/Account.java
src/main/java/com/ehb/banking/Business.java
src/main/java/com/ehb/banking/Transaction.java
```

Note: if `Transaction` is a `record`, `equals()` and `hashCode()` may already be provided by the compiler.

## Things to Think About

While implementing the ticket, consider:

* What is the natural identifier for each domain object?
* Should two `Account` objects with the same account number always be considered equal?
* Does `Transaction` equality matter if transactions are only accessed through an `Account`?
* Should you use `Objects.equals()` and `Objects.hash()` for safety?
* If `Transaction` is a record, is the default equality appropriate?
* What happens if you put `Account` objects in a `HashSet` without overriding `equals()`?

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

* two objects representing the same entity compare as equal
* the same objects behave correctly when placed in a `Set`

## Definition of Done

The ticket is complete when:

* `equals()` and `hashCode()` are implemented (or consciously left as default) for `Account`, `Business`, and `Transaction`.
* The reasoning behind each decision is understood.
* The project successfully compiles with:

```powershell
mvn compile
```

* You can explain why the choices are correct for each class.
