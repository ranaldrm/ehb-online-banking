# EHB Ticket 010 — Implement Payment State Transitions

## Objective

Add behaviour to `Payment` to control how its status changes, rather than allowing arbitrary status assignments.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise state machines, encapsulation, domain invariants, switch expressions, and exception handling.

## Requirements

Support the following valid transitions:

```text
CREATED    → VALIDATED
VALIDATED  → APPROVED
APPROVED   → COMPLETED
```

Any status may transition to:

```text
REJECTED
```

(subject to domain rules you define).

Invalid transitions must be rejected with an appropriate exception.

## Transition Methods

Instead of a general-purpose `setStatus()` method, provide specific methods for each valid transition:

```java
validate()
approve()
complete()
reject()
```

Each method should:

1. Check that the current status allows the transition.
2. Update the status if the transition is valid.
3. Throw a meaningful exception if the transition is not allowed.

## Invalid Transitions

Examples of transitions that must be prevented:

* Approving a payment that has not been validated
* Completing a payment that has not been approved
* Transitioning a `COMPLETED` or `REJECTED` payment to any other state

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
src/main/java/com/ehb/banking/Payment.java
```

You may also introduce a new exception class for invalid state transitions.

## Things to Think About

While implementing the ticket, consider:

* Should transition logic use an `if-else` chain, a `switch` expression, or be delegated to the enum itself?
* Should `PaymentStatus` know which transitions are valid from each state?
* What exception type is most appropriate for an invalid transition — a new custom exception, or an existing one?
* Should `COMPLETED` and `REJECTED` payments be entirely locked from further transitions?
* Is there a case where a validated payment can be rejected? An approved payment?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** connect payment completion to `Account` yet — that is Ticket 012.

Do **not** implement:

* payment validation logic (Ticket 011)
* Spring Boot
* REST controllers
* JPA
* PostgreSQL
* Kafka
* AWS

## Testing

JUnit tests are not required for this ticket.

Verify manually that:

* a valid transition sequence works (e.g. CREATED → VALIDATED → APPROVED → COMPLETED)
* an invalid transition throws an exception
* a completed or rejected payment cannot be transitioned further

## Definition of Done

The ticket is complete when:

* `validate()`, `approve()`, `complete()`, and `reject()` are implemented.
* Valid transitions update the status correctly.
* Invalid transitions throw a meaningful exception.
* There is no public general-purpose `setStatus()` method.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the state machine design and can explain it.
