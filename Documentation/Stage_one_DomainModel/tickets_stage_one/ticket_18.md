# EHB Ticket 018 — Domain Model Completion Check

## Objective

Treat this as the final acceptance ticket for the plain-Java stage of the EHB project.

At this stage, the application is **plain Java 21**. The purpose of this ticket is to confirm that the complete domain model is in place, all tests pass, and the project is ready to become the core of the Spring Boot application.

## Domain Model Checklist

Confirm that the following types exist and are correctly implemented:

### Core Domain Classes

```text
Business
Account
Transaction
Payment
```

### Enums

```text
Currency
TransactionType
PaymentStatus
```

### Supporting Types

```text
PaymentValidator (interface)
Custom exceptions (at least for invalid amounts and insufficient funds)
```

## Acceptance Criteria

Work through each criterion and confirm it is satisfied:

1. **A business can own accounts.**
   A `Business` can have one or more `Account` objects added to it and can retrieve them.

2. **An account starts with a zero balance.**
   A newly created `Account` has a balance of `BigDecimal.ZERO`.

3. **Money can be deposited and withdrawn according to business rules.**
   Valid deposits increase the balance. Valid withdrawals decrease the balance. Invalid operations throw meaningful exceptions.

4. **Successful money movements create transactions.**
   Every successful `deposit()` and `withdraw()` results in a `Transaction` being added to the account's history.

5. **Transactions can be queried using collections and Streams.**
   `getTransactions()`, `getOutgoingTransactions()`, and `getTotalOutgoingPayments()` all behave correctly.

6. **Payments have a controlled lifecycle.**
   A `Payment` moves through `CREATED → VALIDATED → APPROVED → COMPLETED` (or `REJECTED`) via explicit methods. Invalid transitions are rejected.

7. **Invalid operations are rejected through meaningful exceptions.**
   All business-rule violations produce custom exceptions with useful messages. No generic `RuntimeException` or `IllegalArgumentException` is used for expected domain failures.

8. **Core behaviour is covered by JUnit 5 tests.**
   Tests exist for `Account`, `Business`, transaction queries, and the `Payment` lifecycle. Positive and negative cases are both covered.

9. **`mvn test` succeeds.**
   Running the following command completes with zero test failures:

   ```text
   mvn test
   ```

10. **No Spring Boot, REST, database, Kafka, Docker or AWS code has been introduced.**
    The project is plain Java 21 with JUnit 5 only. No framework, infrastructure, or deployment code exists.

## Final Review Steps

Before marking this ticket as done:

* Run `mvn test` and confirm all tests pass.
* Read through each domain class one final time.
* Confirm that the code you have written reflects the domain language of EHB.
* Confirm that you could explain the design of each class to a colleague.

## What Comes Next

Once this ticket is complete, the plain-Java domain model is ready to become the core of the Spring Boot application.

The next stage will introduce:

* Spring Boot project setup
* REST controllers
* JPA persistence
* PostgreSQL
* Further integration and end-to-end testing

None of those belong in this ticket.

## Definition of Done

The ticket is complete when:

* All ten acceptance criteria above are satisfied.
* `mvn test` passes with no failures.
* The codebase contains no Spring Boot, database, Kafka, Docker, or AWS code.
* You are confident in the design and ready to build on top of it.
