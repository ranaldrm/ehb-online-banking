# EHB Online Banking --- Stage 2 Spring Boot & REST Briefing

## Purpose

Stage 2 turns the existing plain-Java EHB domain model into a Spring
Boot REST application.

The goal is **not to rewrite the banking model using Spring**. The
existing domain model should largely survive. Instead, an
application/API layer is added around it so that external clients can
interact with EHB through HTTP.

The central transition is:

``` text
Stage 1

BankingSystem.main()
        │
        ▼
     Business
        │
        ▼
     Account
        │
        ▼
 Payment / Transaction
```

becoming:

``` text
Stage 2

                  HTTP
                   │
                   ▼
             Controller
                   │
                   ▼
                Service
                   │
          ┌────────┴────────┐
          ▼                 ▼
      Business           Account
                            │
                     ┌──────┴──────┐
                     ▼             ▼
                  Payment      Transaction
```

Stage 2 should establish an understanding of Spring Boot, Spring MVC,
REST, JSON, DTOs, dependency injection, validation, exception handling,
and the responsibilities of controller and service layers.

------------------------------------------------------------------------

## 1. Core Learning Objective

At present, Java code directly invokes domain methods. For example, the
`BankingSystem.main()` method constructs objects and calls methods on
them.

By the end of Stage 2, an HTTP request should be able to trigger the
same domain behaviour.

For example:

``` text
POST /api/accounts/123/payments

{
    "targetAccountNumber": "456",
    "amount": 50.00
}
```

could eventually follow this path:

``` text
HTTP request
    ↓
PaymentController
    ↓
PaymentService
    ↓
find source Account
    ↓
find target Account
    ↓
sourceAccount.processOutgoingPayment(...)
    ↓
Payment returned
    ↓
PaymentResponse DTO
    ↓
JSON response
```

The important lesson is understanding how the request travels through
the application and what responsibility belongs at each layer.

------------------------------------------------------------------------

## 2. Stage Boundary: Do Not Introduce PostgreSQL Yet

Stage 2 should continue to store application state **in memory**.

Persistence through JPA, Hibernate, repositories, Flyway, and PostgreSQL
belongs to Stage 3.

For Stage 2, storage can remain conceptually similar to:

``` text
BusinessService
      │
      ▼
Map<String, Business>
```

or use a small in-memory repository abstraction if that becomes useful.

This separation is deliberate. First understand:

``` text
HTTP
 ↓
Spring MVC
 ↓
Controller
 ↓
Service
 ↓
Java domain model
```

before adding:

``` text
JPA
Hibernate
@Entity
@Repository
PostgreSQL
SQL
database transactions
```

This makes it much easier to understand which layer is responsible when
something goes wrong.

------------------------------------------------------------------------

## 3. First Spring Boot Milestone

The first milestone should be deliberately small:

> Start the EHB application as a Spring Boot application and
> successfully retrieve one account through an HTTP GET request.

For example:

``` http
GET /api/accounts/ACC001
```

returning something conceptually like:

``` json
{
  "accountNumber": "ACC001",
  "currency": "GBP",
  "balance": 850.00
}
```

Do **not** begin with payments.

The first endpoint exists primarily to learn the complete Spring request
lifecycle with minimal additional banking logic.

The key question is:

> How did an HTTP request end up retrieving an `Account`?

------------------------------------------------------------------------

## 4. Suggested Stage 2 Tickets

  -----------------------------------------------------------------------
  Ticket                  Goal                    Main learning
  ----------------------- ----------------------- -----------------------
  **S2-01**               Convert Maven project   Boot, dependencies,
                          to Spring Boot          application startup

  **S2-02**               Create first simple     HTTP, Spring MVC,
                          endpoint                `@RestController`

  **S2-03**               Introduce account       DI, IoC, `@Service`,
                          service                 constructor injection

  **S2-04**               Return account DTOs     JSON, Jackson, records,
                                                  API/domain boundaries

  **S2-05**               Add business/account    REST resource design,
                          endpoints               HTTP methods/statuses

  **S2-06**               Expose transaction      Nested resources, DTO
                          history                 mapping

  **S2-07**               Expose payment creation POST, request bodies,
                                                  validation, service
                                                  orchestration

  **S2-08**               Add proper API errors   Exception mapping,
                                                  validation errors, HTTP
                                                  status codes
  -----------------------------------------------------------------------

S2-01 through S2-04 form the essential Spring introduction.

S2-05 onward develops confidence building a more realistic REST
application.

------------------------------------------------------------------------

## 5. S2-01 --- Convert the Existing Project to Spring Boot

The existing project currently uses Java 21, Maven, and JUnit without
Spring dependencies.

The first task is therefore to turn the existing Maven application into
a Spring Boot application rather than creating an unrelated project.

Concepts introduced include:

-   Spring Boot dependencies;
-   `@SpringBootApplication`;
-   `SpringApplication.run(...)`;
-   application context;
-   beans;
-   component scanning;
-   auto-configuration;
-   embedded web server;
-   application startup.

### Important learning goal

Do not simply memorise the annotations.

Understand the distinction between:

**Spring**

A framework providing facilities including dependency injection, an
application container, and Spring MVC.

**Spring Boot**

An opinionated layer around Spring that provides automatic configuration
and sensible defaults, allowing applications to start without large
amounts of manual configuration.

The project should continue to follow the rule:

> Do not treat Spring annotations as magic.

------------------------------------------------------------------------

## 6. S2-02 --- Create the First Controller

Introduce a simple controller, probably beginning with accounts.

Conceptually:

``` text
GET /api/accounts/{accountNumber}
               │
               ▼
        AccountController
```

Likely Spring concepts include:

``` java
@RestController
@RequestMapping(...)
@GetMapping(...)
@PathVariable
```

The important question is not simply how these annotations are written.

It is:

> Why does Spring call this Java method when an HTTP GET request
> arrives?

### HTTP concepts to understand

Common methods:

``` text
GET     retrieve something
POST    create something or request an action
PUT     replace something
PATCH   partially modify something
DELETE  remove something
```

Common status codes:

``` text
200 OK
201 Created
400 Bad Request
404 Not Found
409 Conflict
500 Internal Server Error
```

Understanding HTTP and REST semantics is more important than memorising
annotations.

------------------------------------------------------------------------

## 7. S2-03 --- Introduce the Service Layer

Stage 1 has no service layer. Business logic currently lives in domain
classes such as `Account` and `Business`.

Stage 2 should introduce a service/application layer between controllers
and domain objects.

Avoid allowing controllers to become responsible for everything:

``` text
HTTP handling
+ finding accounts
+ validating payments
+ manipulating balances
+ creating payments
+ exception handling
```

Instead:

``` text
AccountController
       │
       ▼
 AccountService
       │
       ▼
    Account
```

### Responsibilities

**Controller**

Understands the HTTP/API world:

-   requests;
-   response bodies;
-   URL parameters;
-   HTTP status codes;
-   request validation.

**Service**

Understands application use cases and orchestration:

-   finding the objects needed for an operation;
-   coordinating multiple domain objects;
-   invoking domain behaviour.

**Domain objects**

Understand banking rules and invariants.

For example:

``` text
Controller:
"Someone POSTed a request to make a payment."

Service:
"To execute this use case I need the source and target accounts
and need to initiate the payment."

Account:
"These are the rules governing how a payment affects me."
```

This distinction becomes particularly important once persistence is
introduced in Stage 3.

------------------------------------------------------------------------

## 8. Dependency Injection and Inversion of Control

Suppose:

``` text
AccountController
```

requires:

``` text
AccountService
```

Without dependency injection, the controller might construct its
dependency itself:

``` java
private AccountService service = new AccountService();
```

With Spring:

``` text
Spring ApplicationContext
        │
        ├── AccountService
        │
        └── AccountController
                  │
                  └── receives AccountService
```

The controller declares that it requires an `AccountService`, and Spring
constructs and supplies that dependency.

This introduces:

-   dependency injection;
-   inversion of control;
-   Spring beans;
-   application context;
-   constructor injection;
-   loose coupling;
-   testability;
-   replaceable implementations.

Prefer **constructor injection**.

The learning goal is to understand why Spring manages these dependencies
rather than simply learning that `@Service` exists.

------------------------------------------------------------------------

## 9. S2-04 --- DTOs and JSON

The Stage 1 model currently has no DTOs.

Avoid simply returning domain objects directly from controllers.

Instead:

``` text
Account
   ↓
AccountResponse
   ↓
Jackson
   ↓
JSON
```

Possible DTO concepts include:

``` text
AccountResponse
PaymentRequest
PaymentResponse
TransactionResponse
BusinessResponse
```

Java records are particularly suitable for many simple request and
response DTOs.

### Key principle

``` text
Domain model ≠ API model
```

DTOs prevent the public API from becoming tightly coupled to the
internal Java domain model.

This is especially important because `Business` currently contains a
plaintext password field intended for a possible future login feature.
It must not accidentally appear in JSON API responses.

DTOs create an explicit boundary between internal state and externally
exposed data.

------------------------------------------------------------------------

## 10. S2-05 and S2-06 --- Build Out the Read API

Once the basic architecture works, gradually expand the read-only API.

Possible endpoints include:

``` text
GET /api/businesses/{businessId}

GET /api/businesses/{businessId}/accounts

GET /api/accounts/{accountNumber}

GET /api/accounts/{accountNumber}/transactions
```

There is no need to create a large number of endpoints.

The purpose is to practise this flow repeatedly:

``` text
HTTP request
     ↓
Controller
     ↓
Service
     ↓
Domain
     ↓
DTO
     ↓
JSON
```

The existing Stage 1 transaction query methods provide useful
functionality to expose without requiring much additional domain logic.

------------------------------------------------------------------------

## 11. S2-07 --- Expose Payments Through REST

Payments should become the main feature of the later part of Stage 2.

The existing domain model already has a payment workflow:

``` text
CREATED
   ↓
VALIDATED
   ↓
APPROVED
   ↓
COMPLETED
```

with rejection possible before completion.

Expose this existing behaviour through HTTP.

Conceptually:

``` http
POST /api/payments
```

with a request such as:

``` json
{
  "sourceAccountNumber": "ACC001",
  "targetAccountNumber": "ACC002",
  "amount": 125.50
}
```

The application flow becomes:

``` text
POST /api/payments
       │
       ▼
PaymentController
       │
       ▼
PaymentService
       │
       ├── find source account
       ├── find target account
       │
       ▼
Account.processOutgoingPayment(...)
       │
       ▼
Payment
       │
       ▼
PaymentResponse
       │
       ▼
JSON
```

### Important architectural point

The controller should **not** implement payment rules.

The Stage 1 domain model remains responsible for banking behaviour.

Spring provides an external mechanism for invoking that behaviour.

The service layer performs orchestration such as finding the source and
target accounts and coordinating the use case.

------------------------------------------------------------------------

## 12. S2-08 --- Translate Domain Exceptions into HTTP Errors

The Stage 1 project already has an exception hierarchy:

``` text
BankingException
├── AccountNotFoundException
├── DuplicateAccountNumberException
├── ExceedsBalanceException
├── InvalidPaymentException
├── InvalidPaymentTransitionException
└── NonPositiveAmountException
```

These currently exist purely as Java exceptions.

A REST API needs to translate them into HTTP semantics.

For example:

``` text
AccountNotFoundException
        ↓
404 Not Found
```

Invalid input might result in:

``` text
400 Bad Request
```

This introduces Spring concepts such as:

``` java
@RestControllerAdvice
@ExceptionHandler
```

A structured error response could conceptually resemble:

``` json
{
  "status": 404,
  "error": "ACCOUNT_NOT_FOUND",
  "message": "Account ACC999 does not exist"
}
```

The goal is to understand the boundary between domain exceptions and
HTTP/API responses.

------------------------------------------------------------------------

## 13. Request Validation vs Domain Validation

Stage 2 should introduce Jakarta Bean Validation concepts such as:

``` java
@NotNull
@Positive
@NotBlank
```

and Spring's:

``` java
@Valid
```

This creates an important distinction.

### API/input validation

Asks:

> Is this HTTP request structurally acceptable?

For example:

``` text
amount = null
```

could be rejected before the request reaches the domain model.

### Domain validation

Asks:

> Is this banking operation actually legal?

For example:

-   sufficient funds;
-   legal payment state transition;
-   positive monetary amount;
-   account/business invariants.

Conceptually:

``` text
HTTP validation
"Is this request structurally acceptable?"

        ↓

Domain validation
"Is this banking operation actually legal?"
```

Do **not** remove domain safeguards merely because request validation
exists.

The domain model should remain capable of protecting its own invariants.

------------------------------------------------------------------------

## 14. Revisit `CurrencyMatchValidator`

Stage 1 currently has a `CurrencyMatchValidator` whose expected currency
is supplied when the validator is constructed.

This creates a useful design question once Spring's bean lifecycle is
introduced:

> Does it make sense for Spring to manage this validator?

This should not be solved prematurely.

Instead, use it as an exercise for understanding:

-   singleton beans;
-   stateful vs stateless components;
-   configuration;
-   object ownership;
-   which objects should be Spring-managed.

A major lesson of Stage 2 should be:

> Not every Java class should simply receive `@Component`.

------------------------------------------------------------------------

## 15. Testing During Stage 2

Keep the existing Stage 1 domain tests.

Spring should not be required to test whether ordinary domain behaviour
works.

For example:

``` java
account.withdraw(...)
```

should remain testable without starting Spring.

Gradually introduce additional testing layers:

``` text
Domain unit tests
       +
Service tests
       +
Controller / HTTP tests
```

Later Spring testing concepts may include Spring MVC testing and
`MockMvc`.

The objective is to understand which kind of test belongs at which level
rather than immediately introducing large amounts of Spring test
infrastructure.

------------------------------------------------------------------------

## 16. Technologies Deliberately Excluded from Stage 2

Do **not** introduce the following yet:

``` text
PostgreSQL
JPA/Hibernate
Docker
Kafka
microservices
AWS
Spring Security
JWT
OAuth
real authentication
```

These technologies belong to later stages.

Also avoid turning every domain object into a Spring bean.

The domain model should continue to look predominantly like ordinary
Java.

A possible package structure may eventually resemble:

``` text
com.ehb.banking.domain
    Business
    Account
    Payment
    Transaction
    validators...

com.ehb.banking.service
    AccountService
    PaymentService

com.ehb.banking.controller
    AccountController
    PaymentController

com.ehb.banking.dto
    AccountResponse
    PaymentRequest
    PaymentResponse

com.ehb.banking.exception
    ...
```

The exact package structure should evolve naturally rather than being
reorganised all at once.

------------------------------------------------------------------------

## 17. Definition of Stage 2 Completion

Stage 2 is **not** complete merely because a large number of Spring
annotations have been used.

The key outcome is being able to understand and explain this lifecycle:

``` text
curl / Postman / browser
          ↓
       HTTP
          ↓
 embedded server
          ↓
    Spring MVC
          ↓
     Controller
          ↓
       Service
          ↓
    Domain model
          ↓
         DTO
          ↓
      Jackson
          ↓
        JSON
```

By the end of Stage 2, the following questions should be answerable
confidently.

### Who created the controller?

Spring.

### Who supplied its service dependency?

Spring's application context through dependency injection.

### Why doesn't the controller simply call `new AccountService()`?

Because dependencies are being supplied externally, improving separation
of concerns, testability, and configurability.

### Why not return `Account` directly?

Because the API representation should not be tightly coupled to the
internal domain model.

### Where should payment business rules live?

Primarily in the domain model rather than controllers.

### Where should orchestration such as finding the two accounts required for a transfer live?

The service/application layer.

### Where is application state stored during Stage 2?

Still in memory.

Persistence is deliberately postponed until Stage 3.

------------------------------------------------------------------------

## 18. Stage 2 Success Criteria

Stage 2 has succeeded when you can:

-   start and understand the basic structure of a Spring Boot
    application;
-   explain the purpose of the Spring application context;
-   explain dependency injection and inversion of control;
-   understand how Spring discovers and constructs beans;
-   use constructor injection;
-   explain how an HTTP request reaches a controller;
-   create basic REST endpoints;
-   understand common HTTP methods and status codes;
-   separate controller, service, and domain responsibilities;
-   use request and response DTOs;
-   understand how Jackson converts Java objects to/from JSON;
-   validate incoming requests;
-   preserve domain-level validation and invariants;
-   translate domain exceptions into useful HTTP errors;
-   expose existing EHB account, transaction, and payment behaviour
    through REST;
-   test domain behaviour independently of Spring;
-   understand the complete request → controller → service → domain →
    response flow.

The emphasis remains on **understanding the architecture**, not on
building the largest possible banking API.

------------------------------------------------------------------------

## 19. Recommended Starting Point

The next implementation task should be:

### S2-01 --- Convert the Existing Maven Project to Spring Boot

This should be a small, self-contained learning ticket.

Before implementation, understand:

1.  what Spring is;
2.  what Spring Boot adds;
3.  what a Spring bean is;
4.  what the application context is;
5.  what `@SpringBootApplication` does at a high level;
6.  what `SpringApplication.run(...)` starts;
7.  why an embedded web server is now part of the application.

Then modify the existing EHB project yourself rather than replacing it
with generated code.

After S2-01 works, proceed to S2-02 and build the first simple account
endpoint.

------------------------------------------------------------------------

## 20. Overall Stage Progression

``` text
Stage 1 — COMPLETE
Plain Java domain model
        ↓
Stage 2 — CURRENT
Spring Boot + REST
        ↓
Stage 3
JPA + PostgreSQL + testing
        ↓
Stage 4
Microservices + Kafka
        ↓
Stage 5
Docker + AWS + consolidation
```

The purpose of Stage 2 is to create the bridge between the Java domain
knowledge refreshed in Stage 1 and the enterprise Spring/JPA
architecture used in later stages.
