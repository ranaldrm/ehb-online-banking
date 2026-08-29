# EHB Ticket 003 — Introduce Custom Domain Exceptions

## Objective

Replace generic exceptions used for expected business-rule failures with meaningful EHB-specific exceptions.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise exception hierarchies, checked versus unchecked exceptions, and expressive domain error design.

## Requirements

Introduce custom exception classes that represent specific business-rule violations in the EHB domain. At a minimum, cover:

* an invalid monetary amount (e.g. zero or negative deposit/withdrawal)
* insufficient funds (e.g. withdrawal exceeding balance)

Each custom exception should carry a meaningful message describing what went wrong.

## Exception Design Decisions

For each exception, decide whether it should extend:

```java
RuntimeException
```

or another appropriate base class. Consider:

* Will callers be forced to handle it?
* Is it a recoverable or programming error?
* What is the common convention in domain modelling?

## Updating Existing Code

Once the exceptions are defined, update `Account` to throw the new custom exceptions instead of any generic ones currently used.

## Package

Place exceptions in the same package as the other domain classes, or a sub-package if you prefer:

```text
src/main/java/com/ehb/banking/
```

The Java package is:

```java
package com.ehb.banking;
```

## Files

For this ticket, create and/or modify files such as:

```text
src/main/java/com/ehb/banking/InvalidAmountException.java
src/main/java/com/ehb/banking/InsufficientFundsException.java
src/main/java/com/ehb/banking/Account.java
```

The exact names are yours to decide.

## Things to Think About

While implementing the ticket, consider:

* Should exception class names describe the cause (e.g. `InvalidAmountException`) or the action (e.g. `DepositFailedException`)?
* Should there be a single base `BankingException` that others extend?
* Is it useful to include the invalid value in the exception message?
* Should exceptions be checked or unchecked?
* Could any of these exceptions be reused across `deposit()` and `withdraw()`?

Make your own decisions initially. They can be discussed during code review.

## Out of Scope

Do **not** implement the following as part of this ticket:

* `Transaction`
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

You may verify manually that the custom exceptions are thrown and carry readable messages.

## Definition of Done

The ticket is complete when:

* At least two custom domain exceptions exist covering invalid amounts and insufficient funds.
* `Account` throws these custom exceptions instead of generic ones.
* Each exception carries a meaningful message.
* The project successfully compiles with:

```powershell
mvn compile
```

* You understand the exception hierarchy and can explain your design choices.
