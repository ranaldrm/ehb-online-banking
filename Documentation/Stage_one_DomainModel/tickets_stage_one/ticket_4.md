# EHB Ticket 004 — Create the Transaction Model

## Objective

Introduce an immutable or tightly controlled representation of money entering or leaving an account.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise immutable object design, enums, `BigDecimal`, and the Java date/time API.

## Requirements

Create two new types:

```text
Transaction
TransactionType
```

### TransactionType

A `TransactionType` enum should initially distinguish between:

* an incoming transaction (e.g. a deposit)
* an outgoing transaction (e.g. a withdrawal)

### Transaction

A `Transaction` should contain appropriate information including:

* a unique identifier
* the amount
* the transaction type
* the date and time the transaction occurred

Consider which fields should be immutable after construction.

## Immutability

Decide how much of `Transaction` should be immutable. Consider:

* Should the amount ever change after the transaction is created?
* Should the date/time ever change?
* Should the type ever change?
* What Java features support immutability (e.g. `final` fields, records)?

## Package

Place these classes in the same package as the other domain classes:

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
src/main/java/com/ehb/banking/Transaction.java
src/main/java/com/ehb/banking/TransactionType.java
```

## Things to Think About

While implementing the ticket, consider:

* Should `Transaction` be a `record` or an ordinary class?
* What type should the identifier be — `String`, `UUID`, `long`?
* Which Java date/time type is most appropriate — `LocalDateTime`, `Instant`, `ZonedDateTime`?
* Should the transaction generate its own identifier, or receive one from outside?
* Should the transaction capture the date/time automatically at construction, or accept it as a parameter?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** link `Transaction` to `Account` yet. That connection is introduced in Ticket 005.

Do **not** implement:

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

You may verify manually that a `Transaction` can be constructed and its fields read back.

## Definition of Done

The ticket is complete when:

* `TransactionType` contains at least two values: one for incoming and one for outgoing.
* `Transaction` can be constructed with the required information.
* Key fields on `Transaction` are effectively immutable.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the design choices made and can explain them.
