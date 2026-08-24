# Edinburgh Hanoverian Bank (EHB) Online Banking

## Project & Study Briefing

**Status:** Active learning project\
**Target Java version:** Java 21 (LTS)\
**New job start date:** 21 September 2026\
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

A frontend could be added later, but it is not currently a priority.

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

## 16. Current study schedule

The confirmed job start date is **21 September 2026**.

The plan should remain flexible rather than treating dates as hard
deadlines.

### 20--23 August

**Java 21 refresh + domain model**

Goal: regain Java fluency and establish the first plain-Java EHB model.

### 24--30 August

**Spring Boot + REST**

Goal: turn the existing Java concepts into a Spring Boot REST
application.

### 31 August--6 September

**JPA + PostgreSQL + testing**

Goal: persist fictional EHB data and build confidence with Spring/JUnit
testing.

### 7--13 September

**Microservices + Kafka**

Goal: understand service boundaries and basic event-driven
communication.

### 14--20 September

**Docker + AWS + consolidation**

Goal: understand the deployment environment and consolidate the most
important Java/Spring concepts.

### 21 September

**Start new job**

The roadmap does not need to be completely finished for the preparation
to have succeeded.

------------------------------------------------------------------------

## 17. Definition of success before starting

The objective is **not** mastery of every technology.

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
