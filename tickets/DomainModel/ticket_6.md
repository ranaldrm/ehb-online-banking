# EHB Ticket 006 — Query Transaction History

## Objective

Add useful query operations to `Account` for inspecting its transaction history.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise Java Streams, lambdas, `filter`, `map`, `reduce`, and `BigDecimal` aggregation.

## Requirements

Implement the following behaviours on `Account`:

```text
getTransactions()
getOutgoingTransactions()
getTotalOutgoingPayments()
```

Where appropriate, use Java Streams rather than manual loops.

### getTransactions()

Returns the full list of transactions recorded on the account. This was introduced in Ticket 005 — confirm it is already present.

### getOutgoingTransactions()

Returns a list (or collection) containing only the outgoing transactions.

### getTotalOutgoingPayments()

Returns the total sum of all outgoing transaction amounts as a `BigDecimal`.

If there are no outgoing transactions, return `BigDecimal.ZERO`.

## Using Streams

Prefer Stream operations over manual iteration for filtering and aggregating:

```java
// example style — exact implementation is yours to decide
transactions.stream()
    .filter(...)
    .map(...)
    .reduce(...)
```

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

## Things to Think About

While implementing the ticket, consider:

* Should `getOutgoingTransactions()` return a `List`, a `Stream`, or another type?
* How do you sum `BigDecimal` values using Streams?
* Is `reduce` or `collect` more appropriate for the total?
* What should `getTotalOutgoingPayments()` return when there are no transactions?
* Should these query methods be on `Account`, or would a separate query class make sense at this stage?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement the following as part of this ticket:

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

* filtering outgoing transactions returns only withdrawals
* the total outgoing amount sums correctly
* the result is `BigDecimal.ZERO` when there are no outgoing transactions

## Definition of Done

The ticket is complete when:

* `getTransactions()` returns the full transaction history.
* `getOutgoingTransactions()` returns only outgoing transactions.
* `getTotalOutgoingPayments()` returns the correct `BigDecimal` sum.
* Stream operations are used where appropriate.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the Stream operations used and can explain them.
