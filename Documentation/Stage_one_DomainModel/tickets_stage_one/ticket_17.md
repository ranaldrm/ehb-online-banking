# EHB Ticket 017 — Refactor the Domain Model

## Objective

Review and improve the completed plain-Java domain model before introducing Spring Boot.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise critical review of your own code, identifying areas for improvement in structure, naming, and design — without adding new features.

## Requirements

Review the entire domain model and address any issues found in the following areas:

### Package Organisation

* Are classes grouped logically?
* Should exceptions live in a sub-package (e.g. `com.ehb.banking.exception`)?

### Naming

* Are class, method, and variable names clear and consistent?
* Do names reflect the domain language?

### Access Modifiers

* Are fields private where they should be?
* Are methods that should be internal not accidentally `public`?

### Constructors

* Do constructors validate their arguments?
* Is there unnecessary constructor duplication?

### Unnecessary Setters

* Do any classes expose setters that allow external code to violate domain rules?
* Remove or restrict them.

### Mutability

* Are fields that should be immutable marked `final`?
* Are collections properly protected?

### Duplicated Validation

* Is the same validation logic repeated in multiple places?
* Can it be extracted to a shared private method or helper?

### Exception Design

* Are custom exceptions well-named and meaningful?
* Are exception messages useful for debugging?
* Is the exception hierarchy sensible?

### equals() / hashCode()

* Are `equals()` and `hashCode()` implemented consistently?
* If a class is used in a `Set` or `Map`, is equality correctly defined?

### Records

* Are any classes candidates to become records?
* Conversely, are any records inappropriate because they need to be mutable?

### Streams

* Are Stream operations used idiomatically?
* Is there any unnecessary complexity in Stream pipelines?

### Optional

* Is `Optional` used appropriately for values that may be absent?
* Is `Optional` being used as a field type anywhere (which is generally discouraged)?

### Readability and Maintainability

* Is the code easy to read and understand?
* Are there any long methods that should be extracted?

## Process

Work through each class in the domain model:

```text
Account
Business
Transaction
Payment
Currency (enum)
TransactionType (enum)
PaymentStatus (enum)
PaymentValidator
Custom exceptions
```

Make notes on what you find and address each issue.

## Do Not Add Features

This ticket is about quality, not functionality. Do not add new behaviour unless the refactoring reveals a genuine missing requirement.

If you discover something that should be a new feature, note it as a future ticket rather than implementing it here.

## Files

For this ticket, modify any existing domain class as needed.

## Things to Think About

While refactoring, consider:

* Would a fresh reader understand this code without explanation?
* Is every `public` method intentionally public?
* Has any code been written defensively (e.g. null checks for values that should never be null) that obscures the intent?
* Are there any areas of code you do not fully understand yourself?

## Definition of Done

The ticket is complete when:

* Each domain class has been reviewed against the checklist above.
* Identified issues have been addressed or documented as future work.
* All existing tests still pass after refactoring.
* Running `mvn test` succeeds with no failures.
* You can justify each design decision in the revised code.
