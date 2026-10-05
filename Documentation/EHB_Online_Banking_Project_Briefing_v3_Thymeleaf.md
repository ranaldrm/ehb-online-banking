# Edinburgh Hanoverian Bank (EHB) Online Banking

## Project & Study Briefing

**Status:** Active learning project — Stage 2 complete; Stage 2.5 next\
**Target Java version:** Java 21 (LTS)\
**Current date context:** late September 2026\
**Repository:** `ehb-online-banking`

------------------------------------------------------------------------

## 1. Purpose

This is a project-led preparation programme for a new enterprise Java
role.

My professional technical experience has mainly been in Python. I used
Java at university, but I have not previously worked professionally with
Java or built a Spring Boot application.

The preparation stack is:

**Java 21 → Spring Boot → REST APIs → JPA/PostgreSQL → testing →
microservices → Kafka → Docker → AWS**

The project should remain completely independent of my employer. Do not
reference NatWest, RBSI, eQ, internal systems, internal architecture, or
proprietary information.

Edinburgh Hanoverian Bank (EHB) is a **completely fictional bank**
created for this software-development project.

------------------------------------------------------------------------

## 2. Learning approach

This is primarily a **learning project**, not an exercise in generating
an application as quickly as possible.

Act as a senior Java developer, tutor, and code reviewer.

### Tutoring rules

-   I should write the important code myself.
-   Do not implement features or provide complete solutions before I
    attempt them unless I explicitly ask.
-   Prefer explanations, hints, questions, and small examples.
-   Explain compiler errors and stack traces rather than simply fixing
    them.
-   Review code I have written for:
    1.  correctness;
    2.  idiomatic Java 21;
    3.  object-oriented design;
    4.  readability;
    5.  testing;
    6.  maintainability.
-   Point out bugs without immediately replacing my implementation.
-   Explain *why* an approach is idiomatic or unidiomatic.
-   Avoid unnecessary over-engineering.
-   Introduce technologies because the project creates a reason to use
    them, rather than merely to tick boxes.
-   Prefer enterprise-backend relevance over LeetCode-style algorithm
    exercises.
-   Where useful, contrast Java with Python.
-   Where useful, explain modern Java 21 idioms alongside older Java
    styles that may still appear in enterprise code.

When Spring Boot is introduced, explain what Spring is actually doing.
Do not treat annotations as magic.

------------------------------------------------------------------------

## 3. Project concept

The project is called:

**Edinburgh Hanoverian Bank (EHB) Online Banking**

Suggested public GitHub repository:

`ehb-online-banking`

The README should make clear that EHB is fictional.

The project will evolve from a small plain-Java domain model into a more
realistic backend application.

The broad progression is:

``` text
Plain Java domain model
        ↓
Spring Boot
        ↓
REST API
        ↓
JPA / PostgreSQL
        ↓
Testing / production concerns
        ↓
Microservices
        ↓
Kafka
        ↓
Docker
        ↓
AWS
```

A deliberately minimal frontend is now planned immediately after the Spring Boot / REST stage and before persistence. It is a small learning interlude rather than a major frontend-development stage.

The frontend should use **plain HTML and vanilla JavaScript only**, with **no CSS**, frontend framework, Node/npm toolchain, Thymeleaf, Bootstrap, or frontend build system. Its purpose is to make the existing REST capabilities usable from a browser with the least possible additional effort.

------------------------------------------------------------------------

## 4. What the domain model means

The initial domain model does **not** make real financial transactions
and does not connect to any real bank, payment network, database, or
external financial service.

It is simply a Java representation of a fictional banking domain.

Initially, calling something such as:

``` java
account.deposit(new BigDecimal("100.00"));
```

changes the state of Java objects in memory.

For example, it could:

-   increase the fictional account balance;
-   create a `Transaction`;
-   add that transaction to an in-memory `List<Transaction>`;
-   enforce rules such as requiring a positive amount.

Likewise, a withdrawal changes only the state of the fictional
application.

The purpose of the domain model is to practise modelling **things,
relationships, behaviours, and business rules** in Java before
infrastructure such as Spring or databases is introduced.

------------------------------------------------------------------------

## 5. Initial domain model

Keep Version 1 deliberately small.

### `Business`

Represents a fictional organisation that banks with EHB.

Possible responsibilities:

-   business ID;
-   business name;
-   collection of accounts;
-   adding an account;
-   finding an account.

### `Account`

Represents an EHB account.

Possible state:

-   account number;
-   currency;
-   balance;
-   transaction history.

Possible behaviour:

-   deposit;
-   withdraw;
-   get balance;
-   get transactions.

The object should enforce relevant rules rather than exposing its
balance for arbitrary modification.

### `Transaction`

Represents a record of money entering or leaving an account.

Possible state:

-   transaction ID;
-   transaction type;
-   amount;
-   timestamp;
-   reference.

### Initial enums/value concepts

-   `Currency`
-   `TransactionType`

Additional concepts such as `Payment`, `PaymentStatus`, and
`AccountType` should be introduced only when the project needs them.

------------------------------------------------------------------------

## 6. Initial scenario

A useful first milestone is:

``` text
Create fictional business
        ↓
Create GBP account
        ↓
Add account to business
        ↓
Deposit £1,000
        ↓
Withdraw £150
        ↓
Balance = £850
        ↓
Transaction history contains:
£1,000 CREDIT
£150 DEBIT
```

This should remain entirely in memory during the first stage.

It provides practice with:

-   classes and objects;
-   constructors;
-   encapsulation;
-   interfaces where appropriate;
-   enums;
-   `List<T>`;
-   generics;
-   `BigDecimal`;
-   exceptions;
-   relationships between objects;
-   JUnit 5.

------------------------------------------------------------------------

## 7. Java 21 refresh topics

The immediate priority is regaining fluency in everyday Java.

### Core Java

-   classes and objects;
-   constructors;
-   encapsulation and access modifiers;
-   interfaces and abstract classes;
-   inheritance;
-   collections: `List`, `Set`, `Map`;
-   generics;
-   enums;
-   exceptions and custom exceptions;
-   `equals()` and `hashCode()`;
-   `BigDecimal`;
-   Java date/time APIs;
-   Maven;
-   JUnit 5;
-   debugging and reading compiler errors.

### Modern Java

-   lambdas;
-   Streams;
-   `Optional`;
-   records;
-   switch expressions;
-   pattern matching for `instanceof`;
-   pattern matching for `switch`;
-   sealed classes/interfaces;
-   text blocks;
-   `var`.

### Lower-priority Java 21 topic

Understand what **virtual threads** are and why they matter for
I/O-heavy applications, but they are not an immediate implementation
priority.

Do not try to use every Java 21 feature simply because it exists.

------------------------------------------------------------------------

## 8. Streams

Streams should be used where they naturally improve
collection-processing code.

They are likely to appear during the domain-model stage because objects
such as `Business` and `Account` will contain collections.

Good candidate exercises include:

-   find all debit transactions;
-   find transactions above a given amount;
-   calculate total debit value;
-   find an account by account number and return `Optional<Account>`;
-   retrieve the most recent transactions.

Typical operations worth practising:

-   `stream()`;
-   `filter`;
-   `map`;
-   `reduce`;
-   `sorted`;
-   `limit`;
-   `toList`;
-   method references.

Do not force Streams into code where a straightforward method or loop is
clearer.

The goal is to learn **when Streams improve the code**, not merely how
to write Stream syntax.

------------------------------------------------------------------------

## 9. How fictional actions evolve

### Stage 1 --- plain Java

Code invokes domain methods directly:

``` text
Java program
    ↓
Account
    ↓
Transaction
```

All state is in memory.

### Stage 2 --- Spring Boot / REST

HTTP requests trigger application actions:

``` text
HTTP request
     ↓
Controller
     ↓
Service
     ↓
Domain objects
```

Candidate endpoints could eventually include:

``` text
GET  /accounts/{id}
GET  /accounts/{id}/transactions
POST /payments
GET  /payments/{id}
POST /payments/{id}/approve
```

### Stage 2.5 — Minimal Spring MVC + Thymeleaf UI

Stage 2.5 adds a deliberately small browser interface before persistence is
introduced. The purpose is **not** to begin a frontend-development curriculum.
The UI exists to make the application convenient to use while reinforcing
Spring MVC concepts that are relevant to the wider Java/Spring learning goals.

The UI technology is:

- Spring MVC;
- Thymeleaf;
- plain HTML;
- ordinary HTML forms and links.

The existing REST API remains in place. The application therefore has two
presentation paths over the same service/domain logic:

```text
REST client → @RestController → Service → Repository / Domain → JSON

Browser → @Controller → Service → Repository / Domain
                    ↓
                  Model
                    ↓
                Thymeleaf
                    ↓
                   HTML
```

The Thymeleaf UI should allow the user to perform the useful operations already
supported by the application:

- see that the application is running;
- browse the seeded businesses;
- browse/select the seeded accounts;
- view an account and its current balance;
- view transaction history;
- deposit money;
- withdraw money;
- make payments between accounts;
- view a business and its accounts.

The UI controller should normally call the existing service layer directly.
It should **not** make HTTP requests back into the application's own REST API
merely to reuse REST endpoints. Both `@Controller` and `@RestController` are
presentation-layer adapters over the same services.

### Stage 2.5 learning goals

Use the small UI to understand:

- `@Controller` versus `@RestController`;
- Spring MVC `Model`;
- Thymeleaf templates and expressions;
- `th:text`;
- `th:each`;
- `th:href`;
- simple conditional rendering where needed;
- ordinary HTML forms;
- Spring MVC form/request binding;
- POST/Redirect/GET;
- redirects after state-changing operations;
- reusing the same service layer from REST and server-rendered UI controllers.

Thymeleaf itself is a supporting skill rather than a major project objective.
Learn only the subset required to build this UI.

### Stage 2.5 scope limits

Keep this stage deliberately small.

Do **not** introduce:

- CSS;
- JavaScript for normal UI behaviour;
- React, Vue, Angular, Svelte, or another frontend framework;
- npm or Node.js;
- Bootstrap or another UI toolkit;
- a frontend build system;
- authentication or login;
- Spring Security;
- JPA/PostgreSQL before Stage 3;
- new banking functionality merely to make the UI more impressive.

The interface can be visually crude. Browser-default HTML controls are
sufficient.

If collection REST endpoints such as `GET /api/accounts` or
`GET /api/businesses` were already added while beginning the earlier
JavaScript-oriented UI plan, they may remain as legitimate REST capabilities.
The Thymeleaf UI does not need to call them internally.

The UI work is complete once the existing banking operations can be performed
conveniently in the browser, the existing REST API still works, and the main
Spring MVC/Thymeleaf concepts above are understood. Do not continue polishing
the frontend after those goals are met.


### Stage 3 --- persistence

JPA repositories and PostgreSQL store the fictional state:

``` text
HTTP
 ↓
Controller
 ↓
Service
 ↓
Repository
 ↓
PostgreSQL
```

### Later --- events and services

A fictional completed payment could publish an event:

``` text
Payment service
      ↓
PaymentCompleted
      ↓
Kafka
   ↙       ↘
Audit    Notification
```

Everything remains synthetic and confined to the EHB application.

------------------------------------------------------------------------

## 10. Spring Boot stage

Once the plain-Java model is understandable and working, convert the
same project into a Spring Boot application.

Important concepts:

-   dependency injection;
-   inversion of control;
-   beans;
-   application context;
-   component scanning;
-   auto-configuration;
-   controllers;
-   services;
-   repositories;
-   Spring MVC;
-   HTTP;
-   JSON/Jackson;
-   DTOs;
-   request validation;
-   exception handling;
-   constructor injection.

Use a current Spring Boot release compatible with Java 21 and prefer
contemporary practices.

The objective is to understand the path:

``` text
Controller → Service → Repository → Database
```

and what responsibility belongs at each layer.

------------------------------------------------------------------------

## 11. JPA and PostgreSQL stage

Replace in-memory persistence with PostgreSQL.

Topics include:

-   `@Entity`;
-   IDs;
-   entity relationships;
-   Spring Data repositories;
-   transactions;
-   lazy/eager loading;
-   repository queries;
-   DTO vs entity boundaries;
-   the N+1 query problem;
-   Flyway database migrations.

Avoid relying on Hibernate to magically construct the production-style
schema.

------------------------------------------------------------------------

## 12. Testing and production Spring

Develop confidence with:

-   JUnit 5;
-   unit tests;
-   Spring integration tests;
-   validation;
-   global error handling;
-   environment-specific configuration;
-   Spring Boot Actuator;
-   health endpoints;
-   logging.

------------------------------------------------------------------------

## 13. Microservices stage

Potentially split the system into services such as:

``` text
account-service
payment-service
notification-service
```

Use this to explore:

-   service boundaries;
-   data ownership;
-   synchronous REST communication;
-   failure handling;
-   configuration;
-   tracing requests across services;
-   why services should not casually share databases.

The purpose is to understand *why* microservices create architectural
trade-offs, not simply to create lots of small applications.

------------------------------------------------------------------------

## 14. Kafka stage

Introduce event-driven communication using Spring for Apache Kafka.

Topics include:

-   topics;
-   partitions;
-   producers;
-   consumers;
-   consumer groups;
-   offsets;
-   serialization;
-   retries;
-   duplicate delivery;
-   idempotency.

A natural EHB event is:

`PaymentCompleted`

which could be consumed by fictional notification and audit components.

------------------------------------------------------------------------

## 15. Docker and AWS stage

The manager for the new role specifically recommended AWS preparation,
so AWS is an important part of the study plan.

Core AWS concepts to recognise:

-   IAM;
-   EC2;
-   S3;
-   VPC;
-   RDS;
-   CloudWatch;
-   Lambda;
-   ECS;
-   ECR;
-   API Gateway;
-   SQS/SNS;
-   Secrets Manager.

IAM deserves particular attention.

A possible capstone is:

``` text
Spring Boot
    ↓
Docker image
    ↓
Amazon ECR
    ↓
Amazon ECS
   ↙       ↘
RDS      CloudWatch
```

The aim is to understand how an application is built, configured,
containerised, deployed, given permissions, connected to infrastructure,
and monitored.

------------------------------------------------------------------------

## 16. Current project state and revised progression

The original pre-job calendar has now served its purpose and should no longer be treated as the active schedule. Progress should be driven by understanding and available study time rather than the old August/September dates.

Current state:

- **Stage 1 — COMPLETE:** plain Java domain model and JUnit tests.
- **Stage 2 — COMPLETE:** Spring Boot + REST API, service/repository/DTO layers, validation, exception handling, and web-layer tests. Storage is still entirely in memory.
- **Stage 2.5 — NEXT:** minimal HTML/vanilla-JavaScript UI over the existing REST API.
- **Stage 3:** JPA + PostgreSQL + further testing.
- **Stage 4:** microservices + Kafka.
- **Stage 5:** Docker + AWS + consolidation.

The intended progression is now:

``` text
Stage 1 — Plain Java domain model
        COMPLETE
            ↓
Stage 2 — Spring Boot + REST
        COMPLETE
            ↓
Stage 2.5 — Minimal Spring MVC + Thymeleaf UI
            ↓
Stage 3 — JPA + PostgreSQL + testing
            ↓
Stage 4 — Microservices + Kafka
            ↓
Stage 5 — Docker + AWS + consolidation
```

### Stage 2.5 scope guardrails

The UI is intentionally low-effort and should remain a short interlude before persistence.

Use:

- static HTML served by Spring Boot;
- vanilla JavaScript;
- browser `fetch()` calls to the existing REST API;
- standard HTML forms, buttons, selects, tables/lists, and text output;
- likely `src/main/resources/static/index.html` and a small `app.js`.

Do **not** introduce:

- CSS or visual-design work;
- React, Vue, Angular, or another frontend framework;
- Node.js, npm, bundlers, or a frontend build pipeline;
- Thymeleaf or server-side templating;
- Bootstrap or another UI framework;
- authentication/security as part of this stage;
- new banking features merely to make the UI more elaborate.

The UI should normally let the user choose available seeded businesses/accounts from simple selection controls rather than manually entering changing UUIDs or memorising account numbers. This may justify adding simple collection endpoints such as `GET /api/accounts` and `GET /api/businesses`.

A key architectural learning objective is that Stage 3 should be able to replace in-memory repositories with JPA/PostgreSQL while leaving the browser UI largely unchanged, because the UI communicates through the REST API rather than directly with persistence.

------------------------------------------------------------------------

## 17. Ongoing definition of success

The objective is **not** mastery of every technology or completion according to the original pre-job timetable.

A successful preparation period means being able to:

-   read Java 21 comfortably;
-   write ordinary Java without constantly checking basic syntax;
-   understand collections, Streams, lambdas, `Optional`, exceptions,
    and common modern Java idioms;
-   navigate a Maven Java project;
-   understand the basic structure of a Spring Boot application;
-   follow a request through controller → service → repository →
    database;
-   understand dependency injection rather than treating it as
    annotation magic;
-   read and write useful tests;
-   recognise common JPA patterns;
-   understand the purpose of REST APIs;
-   understand what microservices and Kafka are solving;
-   understand enough AWS terminology and architecture to follow
    technical conversations;
-   open an unfamiliar enterprise Java/Spring codebase and begin asking
    informed questions.

------------------------------------------------------------------------

## 18. Development environment

The project is being developed on more than one computer and
synchronised through GitHub.

### Git workflow

At the beginning of a work session on a machine:

``` bash
git pull
```

After completing a coherent piece of work:

``` bash
git status
git add .
git commit -m "Meaningful commit message"
git push
```

Avoid independently changing the same files on multiple computers
without pushing and pulling between machines.

### IDE

Kiro is being used as the primary IDE for this learning project.

Use Kiro's AI primarily as a **tutor and code reviewer**, not as an
autonomous code generator.

The project-level Kiro steering instructions should reinforce the
tutoring rules in this document.

### Toolchain

Target environment:

-   Java 21 JDK;
-   Maven;
-   Git;
-   Kiro;
-   JUnit 5.

Spring Boot, PostgreSQL, Docker, Kafka, and AWS tooling will be added
when those stages are reached.

------------------------------------------------------------------------

## 19. Guidance for continuing this project in a new AI conversation

Treat this document as the source of context for the EHB project.

First establish what has already been implemented before proposing the
next task.

Do not assume that a roadmap item has been completed merely because its
planned date has passed.

When giving me the next task:

1.  keep it small enough for an evening learning session;
2.  state the requirement clearly;
3.  identify the concepts it is intended to practise;
4.  avoid giving the finished implementation;
5.  let me attempt it;
6.  review my solution afterwards;
7.  recommend focused reading only when a knowledge gap appears.

If I ask a conceptual question, explain it in the context of Java 21 and
the EHB project where useful.

The project should remain suitable for a public GitHub repository and
must not contain employer-specific information.
