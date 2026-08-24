# EHB Ticket 011 — Add Payment Validation

## Objective

Introduce explicit payment validation through an interface, keeping validation logic separate from basic data storage.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise interfaces, polymorphism, dependency inversion, and separation of concerns.

## Requirements

Create a `PaymentValidator` interface.

Implement at least one concrete validator class.

### PaymentValidator Interface

Define an interface with an appropriate validation method, for example:

```java
public interface PaymentValidator {
    void validate(Payment payment);
}
```

The method should throw a meaningful exception when validation fails.

### Concrete Validators

Implement at least one validator. Possible validation rules include:

* the payment amount must be positive
* the source account must have sufficient funds to cover the payment
* the payment currency must match the source account's currency

Each validator should focus on a single rule (single responsibility).

## Separation of Concerns

Validation logic should live in validator classes, not inside `Payment` itself.

`Payment` is responsible for holding data and controlling its lifecycle.

Validators are responsible for checking whether a payment is safe to proceed.

## Package

Place these classes in the same package as the other domain classes, or a sub-package if you prefer:

```text
src/main/java/com/ehb/banking/
```

The Java package is:

```java
package com.ehb.banking;
```

## Files

For this ticket, create files such as:

```text
src/main/java/com/ehb/banking/PaymentValidator.java
src/main/java/com/ehb/banking/AmountValidator.java
src/main/java/com/ehb/banking/SufficientFundsValidator.java
```

The exact names are yours to decide.

## Things to Think About

While implementing the ticket, consider:

* Should the validator method return a boolean, a result object, or throw an exception?
* Should there be a composite validator that runs multiple validators in sequence?
* Should validators be stateless (and therefore safely reusable)?
* How would you test a validator in isolation?
* How does this design support adding new validation rules in the future without modifying existing classes?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** connect validators to the payment completion flow yet — that is Ticket 012.

Do **not** implement:

* Spring Boot
* REST controllers
* JPA
* PostgreSQL
* Kafka
* AWS

## Testing

JUnit tests are not required for this ticket.

Verify manually that:

* a valid payment passes validation
* a payment with an invalid amount fails validation
* a payment where the account has insufficient funds fails validation

## Definition of Done

The ticket is complete when:

* `PaymentValidator` interface exists with an appropriate validation method.
* At least one concrete validator is implemented.
* Validators are separate from `Payment`.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the interface-based design and can explain the separation of concerns.
