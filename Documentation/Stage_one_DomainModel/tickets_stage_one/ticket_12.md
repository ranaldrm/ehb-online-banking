# EHB Ticket 012 — Complete an Approved Payment

## Objective

Connect payments to account behaviour so that completing an approved payment withdraws money and records a transaction.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise collaboration between domain objects, enforcing invariants, failure handling, and atomic business operations.

## Requirements

When an approved payment is completed:

1. The appropriate amount is withdrawn from the source account.
2. An outgoing transaction is recorded on the account.
3. The payment status transitions to `COMPLETED`.

The model must prevent:

* Completing a payment that has not been approved.
* Completing a payment that has already been completed.
* Completing a payment that has been rejected.
* Completing a payment when the account has insufficient funds.

If any of these conditions occur, an appropriate exception must be thrown and no changes to the account balance or transaction history must take place.

## Atomicity

The operation must succeed or fail as a whole. If the withdrawal fails (e.g. due to insufficient funds), the payment status must **not** be changed to `COMPLETED`.

Consider the order of operations:

1. Check that the payment is in the correct state for completion.
2. Perform the withdrawal (which may fail).
3. Only after the withdrawal succeeds, transition the payment to `COMPLETED`.

## Where Should the Logic Live?

Decide whether the completion logic belongs:

* inside `Payment` (calling `account.withdraw()` itself)
* inside `Account`
* in a separate service or coordinator class

There is no single correct answer — think through the trade-offs.

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

For this ticket, modify files such as:

```text
src/main/java/com/ehb/banking/Payment.java
src/main/java/com/ehb/banking/Account.java
```

You may also introduce a coordinator or service class if appropriate.

## Things to Think About

While implementing the ticket, consider:

* What is the correct order of operations to avoid partial state changes?
* Should a completed payment be able to be completed again? How do you prevent this?
* Should `Payment` directly call methods on `Account`, or should a third party coordinate?
* What happens if the `complete()` call succeeds but the withdrawal throws an exception?
* Is the withdrawal creating a `Transaction` sufficient, or should the `Payment` also hold a reference to it?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement the following as part of this ticket:

* Spring Boot
* REST controllers
* JPA
* PostgreSQL
* Kafka
* AWS

## Testing

JUnit tests are not required for this ticket.

Verify manually that:

* completing an approved payment withdraws the correct amount
* the account transaction history contains the outgoing transaction
* the payment status is `COMPLETED`
* attempting to complete a non-approved payment throws an exception
* attempting to complete a payment with insufficient funds throws an exception and leaves the balance unchanged

## Definition of Done

The ticket is complete when:

* Completing an approved payment deducts the amount from the source account.
* An outgoing transaction is recorded on the account.
* The payment transitions to `COMPLETED`.
* Invalid completion attempts throw meaningful exceptions without side effects.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the interaction between `Payment` and `Account` and can explain the design.
