# EHB Ticket 009 — Introduce the Payment Model

## Objective

Add payments as a distinct domain concept representing a requested outgoing payment.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise domain modelling, enums with lifecycle states, object relationships, and `BigDecimal`.

## Requirements

Create two new types:

```text
Payment
PaymentStatus
```

### PaymentStatus

A `PaymentStatus` enum should represent the lifecycle of a payment:

```text
CREATED
VALIDATED
APPROVED
COMPLETED
REJECTED
```

### Payment

A `Payment` should contain appropriate information including:

* a unique identifier
* the source account
* the amount
* the current status
* the date/time the payment was created

## Payment Lifecycle

A payment begins in the `CREATED` state when it is first constructed.

The status will be controlled through explicit transitions in Ticket 010. For this ticket, focus on the data model — ensure the status field exists and is readable.

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
src/main/java/com/ehb/banking/Payment.java
src/main/java/com/ehb/banking/PaymentStatus.java
```

## Things to Think About

While implementing the ticket, consider:

* Should `Payment` be a `record` or an ordinary class? (Hint: records are immutable — is that appropriate here?)
* What type should the identifier be — `String`, `UUID`, `long`?
* Should `Payment` hold a reference to the `Account` object, or just the account number?
* Should the amount be validated at construction?
* Should the initial status always be `CREATED`, or should it be passed in?
* Which Java date/time type is most appropriate for the creation timestamp?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement state transitions yet — that is Ticket 010.

Do **not** implement:

* payment validation logic (Ticket 011)
* payment completion connecting to `Account` (Ticket 012)
* Spring Boot
* REST controllers
* JPA
* PostgreSQL
* Kafka
* AWS

## Testing

JUnit tests are not required for this ticket.

Verify manually that a `Payment` can be constructed and its fields read back.

## Definition of Done

The ticket is complete when:

* `PaymentStatus` contains all five lifecycle values.
* `Payment` can be constructed with the required information.
* A new `Payment` starts with `CREATED` status.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the design choices made and can explain them.
