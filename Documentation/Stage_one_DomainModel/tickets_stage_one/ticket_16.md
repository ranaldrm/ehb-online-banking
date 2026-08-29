# EHB Ticket 016 — Test Payment Lifecycle

## Objective

Write unit tests covering the full lifecycle of a payment, including valid and invalid state transitions, validation, and completion.

At this stage, the application is **plain Java 21** with JUnit 5 (introduced in Ticket 013). Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise testing stateful domain behaviour, negative test cases, and exception assertions.

## Requirements

### Payment Creation

* A new payment starts in the `CREATED` status.

### Valid State Transitions

* A `CREATED` payment can be validated — status becomes `VALIDATED`.
* A `VALIDATED` payment can be approved — status becomes `APPROVED`.
* An `APPROVED` payment can be completed — status becomes `COMPLETED`.
* Any in-progress payment can be rejected — status becomes `REJECTED`.

### Invalid State Transitions

* Approving a `CREATED` payment throws an exception.
* Completing a `VALIDATED` payment throws an exception.
* Completing a `REJECTED` payment throws an exception.
* Transitioning a `COMPLETED` payment to any other status throws an exception.
* Transitioning a `REJECTED` payment to any other status throws an exception.

### Validation Failure

* A payment with an invalid amount (zero or negative) fails validation with an appropriate exception.
* A payment where the account has insufficient funds fails validation with an appropriate exception.

### Successful Completion

* Completing an approved payment deducts the correct amount from the source account.
* Completing an approved payment records an outgoing transaction on the account.
* The payment status is `COMPLETED` after successful completion.

### Prevention of Duplicate Completion

* Calling `complete()` on an already-completed payment throws an exception.

### Insufficient Funds on Completion

* Attempting to complete a payment when the account has insufficient funds throws an exception.
* The account balance remains unchanged after such a failure.
* The payment status does not change after such a failure.

## Test Structure

Place tests in:

```text
src/test/java/com/ehb/banking/PaymentTest.java
```

Example structure:

```java
@Test
void approvedPaymentCanBeCompleted() {
    Account account = new Account("12345678", Currency.GBP);
    account.deposit(new BigDecimal("500.00"));

    Payment payment = new Payment(account, new BigDecimal("200.00"));
    payment.validate();
    payment.approve();
    payment.complete();

    assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    assertEquals(new BigDecimal("300.00"), account.getBalance());
}
```

Adapt to match your actual implementation.

## Things to Think About

While writing the tests, consider:

* Do your tests check the exception type, the message, or both?
* Are you testing that the account balance is unchanged after a failed completion?
* Are you testing that no transaction is recorded after a failed operation?
* Should `@BeforeEach` set up a common `Account` and `Payment`, or is per-test setup cleaner?
* Are negative test cases as important as positive ones?

## Out of Scope

Do **not** implement new features in this ticket. Tests should cover behaviour already implemented in Tickets 009–012.

Do **not** implement:

* Spring Boot
* Spring Test
* JPA
* PostgreSQL
* Kafka
* AWS

## Definition of Done

The ticket is complete when:

* All test cases listed above are implemented and pass.
* Running `mvn test` succeeds with no failures.
* Both positive (success) and negative (failure) cases are covered.
* Tests are independent and clearly named.
* You can explain what each test is verifying and why.
