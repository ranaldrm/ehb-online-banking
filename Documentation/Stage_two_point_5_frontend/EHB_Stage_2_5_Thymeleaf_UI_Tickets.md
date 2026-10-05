# EHB Stage 2.5 — Minimal Thymeleaf UI

## Ticket Pack

**Project:** Edinburgh Hanoverian Bank (EHB) Online Banking  
**Stage:** 2.5 — Minimal server-rendered browser UI  
**Starting point:** Stage 2 Spring Boot + REST complete; storage remains in memory  
**UI technology:** Spring MVC + Thymeleaf + plain HTML  
**Technology constraint:** **No CSS and no JavaScript unless a later requirement genuinely makes a tiny amount unavoidable.**

---

## 1. Purpose of this stage

Stage 2.5 adds a deliberately small browser interface to the existing Spring Boot application before persistence is introduced.

The original Stage 2.5 plan used plain HTML plus JavaScript `fetch()` calls. That approach has been replaced because learning JavaScript from scratch would be a larger detour from the main purpose of EHB: learning Java, Spring Boot, backend development, persistence, and related enterprise technologies.

The revised UI should therefore reinforce **Spring MVC** rather than introduce a separate frontend programming curriculum.

The UI should ultimately allow the user to perform the useful operations currently available through the application:

- see that the application is running;
- browse the seeded businesses;
- browse/select the seeded accounts;
- view an account and its current balance;
- view an account's transaction history;
- deposit money into an account;
- withdraw money from an account;
- make a payment between accounts;
- view a business and its accounts.

The existing REST API remains part of the application. **Do not remove or convert the existing `@RestController` classes.**

---

## 2. Target architecture

The existing REST path remains:

```text
Postman / API client
        ↓ HTTP / JSON
@RestController
        ↓
Service
        ↓
Repository / Domain
```

The new browser UI introduces a parallel presentation path:

```text
Browser
   ↓ HTTP
@Controller
   ↓
Service
   ↓
Repository / Domain
   ↓
Model
   ↓
Thymeleaf template
   ↓
HTML returned to browser
```

For form submissions:

```text
HTML form
   ↓ POST
@Controller
   ↓
Service / Domain
   ↓
redirect
   ↓
GET page again
   ↓
fresh model data rendered by Thymeleaf
```

A major learning goal is to understand the difference between:

```text
@RestController → data (usually JSON)
@Controller     → view/template
```

and why both can coexist in one Spring Boot application.

---

## 3. Hard scope limits

Do **not** introduce:

- CSS;
- JavaScript for normal UI behaviour;
- React, Vue, Angular, Svelte, or another frontend framework;
- npm or Node.js;
- Bootstrap or another UI toolkit;
- a frontend build system;
- authentication or login;
- Spring Security;
- database persistence;
- JPA or PostgreSQL;
- new banking functionality merely to make the UI more impressive.

Do not redesign the existing REST API simply because the UI now exists.

Do not make Thymeleaf a large independent study topic. Learn only the small subset needed to render data, iterate over collections, bind simple values into links/forms, and display results.

---

## 4. Important change from the abandoned JavaScript ticket plan

If you already completed backend collection endpoints such as:

```text
GET /api/accounts
GET /api/businesses
```

keep them. They are legitimate REST capabilities and do not need to be removed.

However, the Thymeleaf UI does **not** need to call those REST endpoints internally.

The UI controller may call the existing **service layer directly**:

```text
UI Controller → Service
REST Controller → Service
```

This is not bypassing the application's architecture. Both controllers are presentation-layer adapters over the same application services.

Do **not** make the Spring server send HTTP requests to its own REST API merely to reuse an endpoint.

---

## 5. Learning approach

These tickets are intentionally guided.

For each ticket:

1. inspect the named existing code first;
2. understand the new Spring/Thymeleaf concept before using it;
3. implement the important code yourself;
4. use hints rather than requesting a complete implementation where possible;
5. manually verify the browser behaviour;
6. run `mvn test` after meaningful backend changes;
7. keep the UI ugly and small.

The aim is not to become a Thymeleaf specialist. The aim is to learn a useful slice of Spring MVC while obtaining a convenient UI.

---

# EHB Ticket TH-01 — Add Thymeleaf to the Project

## Objective

Add Thymeleaf support to the existing Spring Boot application and confirm that the project still builds and all existing tests pass.

## Existing code/files to inspect

Before editing anything, inspect:

- `pom.xml`;
- the existing Spring Boot dependencies;
- `EhbBankingApplication`;
- the current `src/main/resources` directory.

## Requirements

1. Add the appropriate Spring Boot Thymeleaf starter dependency.
2. Do not remove `spring-boot-starter-web`.
3. Do not change the existing REST controllers.
4. Run the full test suite.
5. Start the application and confirm the existing REST endpoints still work.

## Concepts being practised

- Maven dependencies;
- Spring Boot starters;
- auto-configuration;
- adding a server-side template engine to an existing Spring MVC application.

## Questions you should be able to answer

- What does a Spring Boot starter provide?
- Why does adding Thymeleaf not require manually constructing a template engine?
- Why do the REST endpoints continue to work?

## Verification

Run:

```bash
mvn test
```

Then start the application and verify at least one existing endpoint, for example:

```text
GET /api/status
```

Do not create the UI yet.

---

# EHB Ticket TH-02 — Render the First Thymeleaf Page

## Objective

Create the simplest possible server-rendered EHB page.

The page does not need banking functionality yet.

## New concepts

This ticket introduces:

- `@Controller`;
- a controller method returning a view name;
- `src/main/resources/templates`;
- a Thymeleaf HTML template.

## Requirements

1. Create a new UI controller in an appropriate controller package.
2. Annotate it with `@Controller`, **not** `@RestController`.
3. Add a GET route for the main UI, for example `/` or `/banking`.
4. Create a corresponding HTML template under:

```text
src/main/resources/templates/
```

5. The page should contain an `EHB Online Banking` heading.
6. No CSS.
7. No JavaScript.

## Important comparison

Compare these two ideas:

```java
@RestController
```

and:

```java
@Controller
```

Your existing REST controllers return response data. The new UI controller should return a **view name** that Spring resolves to a Thymeleaf template.

## Starting hint

The controller method does not need to retrieve any banking data yet.

Think about what a returned string such as a template name means when the class is a normal `@Controller`.

## Verification

Start the application and visit the UI route in a browser.

You should see ordinary HTML generated from the Thymeleaf template.

Confirm that `/api/...` routes still behave as REST endpoints.

---

# EHB Ticket TH-03 — Pass Data Through the Spring MVC Model

## Objective

Pass a small value from Java into the HTML page using Spring's MVC `Model` and Thymeleaf.

This ticket deliberately uses a trivial value before introducing account collections.

## Requirements

1. Add a `Model` parameter to the UI controller method.
2. Put a simple value into the model, such as an application/page status message.
3. Display that value in the Thymeleaf template using `th:text`.
4. Do not hard-code the displayed value in the HTML.

## Concepts being practised

- Spring MVC `Model`;
- model attributes;
- server-side rendering;
- Thymeleaf expressions;
- `th:text`;
- the lifecycle:

```text
Controller creates model data
        ↓
Spring passes model to Thymeleaf
        ↓
Thymeleaf renders HTML
        ↓
browser receives finished HTML
```

## Reflection question

How is this different from the abandoned JavaScript approach, where the browser would load a page and then separately call an API with `fetch()`?

## Verification

Change the Java-side value temporarily and reload the browser.

The rendered page should reflect the Java-side value.

---

# EHB Ticket TH-04 — Display All Accounts

## Objective

Use the existing account service to supply all accounts to the Thymeleaf page and display them.

## Existing code to inspect

Review:

- `AccountService`;
- `AccountRepository`;
- `AccountResponse`;
- any `getAllAccounts`/collection method you added while starting the previous UI plan.

If you already completed the REST `GET /api/accounts` work, reuse the **service capability**, not the REST controller.

## Requirements

1. The UI controller obtains the account collection through `AccountService`.
2. Do not access `AccountRepository` directly from the controller.
3. Add the account collection to the MVC model.
4. Render all accounts in the template using Thymeleaf iteration.
5. Display at least:
   - account number;
   - currency;
   - balance.
6. A plain HTML table or list is sufficient.

## Thymeleaf concept

Investigate:

```text
th:each
```

Understand the idea before using it.

Conceptually:

```text
for each account in accounts
    render this HTML element
```

## Architecture requirement

Keep:

```text
UI Controller → AccountService → AccountRepository
```

Do not create a second account repository or duplicate seeded data for the UI.

## Verification

Reload the page and confirm that all five seeded accounts appear.

Restart the application and confirm the page obtains the data again from the backend.

---

# EHB Ticket TH-05 — Add Account Selection and an Account Details Page

## Objective

Allow the user to choose an account and navigate to a page showing its current details.

## Suggested route shape

A natural UI route would be something like:

```text
GET /accounts/{accountNumber}
```

This is a **UI route**, distinct from the existing REST route under `/api/accounts/...`.

You may choose another sensible UI route if it avoids ambiguity.

## Requirements

1. Each account shown on the main page should provide a way to view that account.
2. The account number should be included in the generated link.
3. Add a UI controller method with a path variable.
4. Use `AccountService` to retrieve the account.
5. Put the required data into the model.
6. Render an account-details template.
7. Display at least:
   - account number;
   - currency;
   - balance.

## Concepts being practised

- `@PathVariable` in an MVC controller;
- dynamic links;
- Thymeleaf `th:href`;
- multiple templates;
- retrieving current server state for a page.

## Important comparison

You already used `@PathVariable` in REST controllers.

Ask yourself what is the same and what is different when the controller returns a template rather than a DTO.

## Verification

Navigate from the account list to several different accounts.

The URL and displayed account should correspond.

---

# EHB Ticket TH-06 — Display Transaction History

## Objective

Display the selected account's transaction history on its account page.

## Existing code to inspect

Review:

- `AccountService.getTransactionHistory(...)`;
- `TransactionResponse`;
- the existing REST transaction-history controller method.

## Requirements

1. Retrieve the transaction history through the service layer.
2. Add it to the model for the account-details page.
3. Iterate over the transactions with Thymeleaf.
4. Display useful fields such as:
   - transaction type;
   - amount;
   - timestamp.
5. If the collection is empty, display a simple message such as:

```text
No transactions
```

## Thymeleaf concepts

This ticket may introduce:

- another `th:each`;
- `th:if` and/or `th:unless`.

Do not try to learn all Thymeleaf conditional features. Use only what the page requires.

## Verification

View accounts with different transaction histories and confirm the displayed data corresponds to backend state.

---

# EHB Ticket TH-07 — Display Businesses and Their Accounts

## Objective

Add a simple business section/page that lets you browse the seeded businesses and see their accounts without manually copying their changing UUIDs.

## Existing code to inspect

Review:

- `BusinessService`;
- `BusinessRepository`;
- `BusinessResponse`;
- any collection method added while beginning the previous UI plan.

## Requirements

1. Obtain all businesses through `BusinessService`.
2. Do not access `BusinessRepository` directly from the UI controller.
3. Display the business names.
4. Provide a way to view a selected business.
5. The selected business page/section should show:
   - business name;
   - business ID;
   - its accounts.
6. The plaintext password must never be displayed.

## Concepts being practised

- multiple model collections;
- UUIDs in routes;
- reusing service-layer functionality from more than one presentation layer;
- protecting fields at presentation boundaries.

## Verification

Restart the application so business UUIDs change.

The UI should continue to work because it obtains the current IDs from backend data rather than hard-coding them.

---

# EHB Ticket TH-08 — Add a Deposit Form

## Objective

Allow a deposit to be made from the account page using a normal HTML form and a Spring MVC POST handler.

No JavaScript is required.

## Target interaction

Conceptually:

```text
Account page
   ↓
user enters amount
   ↓
HTML form POST
   ↓
UI controller
   ↓
AccountService.deposit(...)
   ↓
redirect to account page
   ↓
fresh balance/history displayed
```

## Requirements

1. Add a plain HTML form to the account page.
2. The form must identify:
   - the account being changed;
   - the deposit amount.
3. Add an MVC POST handler for the form submission.
4. Convert/bind the submitted amount into an appropriate Java type.
5. Call the existing `AccountService.deposit(...)`.
6. Do **not** duplicate deposit business logic in the controller.
7. After success, redirect back to the account page.
8. The refreshed page should show the new balance and transaction.

## Concepts being practised

- HTML `<form>`;
- `method="post"`;
- form parameters;
- `@RequestParam` or an appropriate simple form-binding technique;
- POST/Redirect/GET;
- `redirect:`;
- reusing service/domain behaviour.

## Important concept: POST/Redirect/GET

Understand why returning a redirect after a successful mutation is preferable to simply rendering the same page directly from the POST handler.

Consider what could happen if a browser refreshed a page whose last request was a POST.

## Scope warning

Do not create a complicated form DTO unless it genuinely improves clarity. This UI is deliberately small.

## Verification

1. Open an account page.
2. Note the balance.
3. Deposit a small amount.
4. Confirm you are returned to the account page.
5. Confirm the balance and transaction history changed.
6. Refresh the page and make sure the deposit is **not** submitted again.

---

# EHB Ticket TH-09 — Add a Withdrawal Form

## Objective

Allow a withdrawal from the account page using the same basic Spring MVC form pattern.

## Requirements

1. Add a withdrawal form.
2. Add a corresponding POST handler.
3. Use `AccountService.withdraw(...)`.
4. Redirect back to the account page after success.
5. Do not reproduce sufficient-funds or positivity rules in the HTML/controller as the authoritative validation.
6. Existing domain behaviour must remain responsible for banking rules.

## Learning goal

This ticket should feel substantially easier than TH-08.

Recognise the repeated pattern:

```text
form → controller POST → service → redirect → GET
```

Do not immediately build a generic form-processing abstraction simply because deposit and withdrawal look similar.

## Verification

Test:

- a valid withdrawal;
- a withdrawal larger than the balance.

For the valid case, confirm the refreshed account page shows the changed state.

The failure case will be improved in the next ticket.

---

# EHB Ticket TH-10 — Show Simple Form Errors

## Objective

Make failed deposit/withdrawal operations understandable in the browser without creating a sophisticated error system.

## Background

Your REST API already has `GlobalExceptionHandler`, but the Thymeleaf UI is a separate presentation path. A REST error response containing JSON is not a good user-facing HTML page.

For this small UI, use the simplest understandable MVC approach.

## Requirements

1. A domain error during a deposit or withdrawal should not leave the user staring at JSON or a generic server error page.
2. Display a short error message on an HTML page.
3. Keep domain rules in the domain/service layer.
4. Do not duplicate exception-to-message logic throughout many controller methods if a very small shared MVC solution is clearer.
5. Do not redesign the existing REST `GlobalExceptionHandler`.
6. Keep the solution proportionate to this tiny UI.

## Concepts to investigate

Depending on the implementation you choose, this may introduce one of:

- catching a known banking exception in a UI controller and re-rendering/redirecting appropriately;
- a small MVC-oriented `@ControllerAdvice`;
- redirect attributes / flash attributes.

Ask for guidance before introducing a more elaborate mechanism.

## Verification

Deliberately attempt:

- a non-positive amount;
- a withdrawal exceeding the balance.

The browser should display a useful message and remain usable.

---

# EHB Ticket TH-11 — Add the Payment Form

## Objective

Allow a user to make a payment between accounts using a server-rendered HTML form.

## Existing code to inspect

Review:

- `PaymentService`;
- `PaymentRequest`;
- `PaymentResponse`;
- `PaymentController`;
- the account collection already supplied to the UI.

## Requirements

The payment form should provide:

- source-account selection;
- target-account selection;
- amount;
- submit button.

Use ordinary HTML `<select>` elements populated by Thymeleaf from backend account data.

On submission:

1. Spring receives the form fields;
2. the UI controller obtains the submitted values;
3. call the existing `PaymentService.processPayment(...)`;
4. do not reproduce payment validation in the UI controller;
5. after success, show a simple confirmation and/or redirect to an appropriate page;
6. subsequent account pages must show the changed source and target balances/transactions.

## Thymeleaf concepts

This ticket reinforces:

- `th:each` inside `<option>`;
- `th:value`;
- `th:text`;
- server-generated form controls.

## Important distinction

The browser is submitting **form data**, not JSON.

Be able to contrast:

```text
REST API:
JSON → Jackson → PaymentRequest
```

with the simple MVC UI path:

```text
HTML form fields → Spring MVC binding → controller parameters → PaymentService
```

Both ultimately reach the same service/domain behaviour.

## Verification

Make a valid payment between compatible accounts.

Confirm:

- source balance decreases;
- target balance increases;
- transaction histories update.

Also try a known failure such as insufficient funds or incompatible currencies and confirm the UI handles it sensibly.

---

# EHB Ticket TH-12 — Add Simple Navigation and Consolidate the Templates

## Objective

Make the crude UI easy enough to move around without introducing styling or frontend complexity.

## Requirements

Add ordinary HTML links where useful, for example:

- home;
- accounts;
- businesses;
- back to selected account.

Review the templates for obvious duplication and confusing structure.

You may use a small amount of Thymeleaf template reuse **only if it clearly simplifies the project**.

## Scope warning

Do not turn this into a Thymeleaf-layout exercise.

You do **not** need:

- a template-layout library;
- CSS;
- navigation bars;
- responsive design;
- fragments for every repeated HTML element.

Plain links such as:

```html
<a>Home</a>
```

are sufficient.

## Concepts being practised

- server-generated links;
- `th:href`;
- keeping a small multi-page server-rendered application understandable;
- resisting unnecessary abstraction.

## Verification

Perform the major operations using only browser links/forms, without manually typing application URLs after entering the UI.

---

# EHB Ticket TH-13 — Preserve and Verify the REST API

## Objective

Confirm that adding the Thymeleaf presentation layer has not damaged the REST API built during Stage 2.

## Why this matters

The project should now have **two presentation mechanisms** over the same application logic:

```text
REST client → @RestController ┐
                              ├→ Services → Repository → Domain
Browser UI → @Controller ─────┘
```

Neither should replace the other.

## Requirements

1. Run:

```bash
mvn test
```

2. Manually verify representative REST endpoints with Postman or another API client:
   - account lookup;
   - transaction history;
   - deposit or withdrawal;
   - payment;
   - business lookup.
3. Confirm REST endpoints still return their expected JSON/plain-text forms.
4. Confirm the browser UI returns HTML.
5. Do not make REST controllers depend on UI controllers or Thymeleaf.

## Reflection questions

Be able to explain:

- Why can both controller types call the same service?
- Why shouldn't the Thymeleaf controller call the REST controller?
- Why shouldn't the server make HTTP calls to itself merely to reach its own service functionality?
- Which layer contains the actual banking rules?

---

# EHB Ticket TH-14 — Stage 2.5 End-to-End Checkpoint

## Objective

Verify the complete Thymeleaf UI and consolidate what you learned before moving to JPA/PostgreSQL.

Do not add new features during this ticket unless verification exposes a genuine bug.

## Preparation

Restart the application to restore the known in-memory seed state.

Run:

```bash
mvn test
```

The existing Stage 1 and Stage 2 tests must remain green.

## Browser verification scenario

Using the browser UI:

1. open the EHB home page;
2. confirm the seeded accounts are visible/selectable;
3. confirm the seeded businesses are visible/selectable;
4. view a business and its accounts;
5. open an account and note its balance;
6. view its transaction history;
7. make a deposit;
8. confirm the redirected account page shows the updated balance/history;
9. make a valid withdrawal;
10. confirm the updated state;
11. make a valid payment between compatible accounts;
12. inspect source and target accounts afterwards;
13. deliberately trigger an insufficient-funds error;
14. confirm the UI reports the failure and account state remains valid.

## Architecture checkpoint

At the end of Stage 2.5, be able to explain the deposit flow:

```text
Browser requests account page
        ↓
Spring MVC routes GET to @Controller
        ↓
Controller calls AccountService
        ↓
Controller puts account/transactions into Model
        ↓
Thymeleaf renders HTML
        ↓
Browser displays page

User submits deposit form
        ↓
Browser sends HTTP POST form data
        ↓
Spring MVC routes POST to @Controller
        ↓
Controller receives account number + amount
        ↓
Controller calls AccountService.deposit(...)
        ↓
Service obtains Account from repository
        ↓
Account domain method applies business rules
        ↓
Controller returns redirect
        ↓
Browser performs fresh GET
        ↓
current backend state is rendered by Thymeleaf
```

Also be able to explain the difference between this and the existing REST flow:

```text
REST:
Controller → DTO → Jackson → JSON

Thymeleaf UI:
Controller → Model → Thymeleaf → HTML
```

## Completion criteria

Stage 2.5 is complete when:

- the main existing banking operations can be performed through the browser;
- no JavaScript is required for normal operation;
- no CSS or frontend framework has been introduced;
- the existing REST API remains functional;
- both REST and UI controllers reuse the service layer;
- domain rules remain in the backend/domain rather than HTML;
- no database has been introduced;
- the test suite remains green;
- you understand the basic roles of `@Controller`, `Model`, Thymeleaf templates, HTML forms, and redirects;
- the UI is crude but usable.

A suitable commit message might be:

```text
Add minimal Thymeleaf banking UI
```

---

## 6. Stage 2.5 outcome and transition to Stage 3

At completion, the architecture should conceptually be:

```text
                    ┌→ REST controllers → JSON
External requests ──┤
                    └→ MVC controllers → Thymeleaf → HTML
                              │
                              ▼
                           Services
                              │
                              ▼
                     In-memory repositories
                              │
                              ▼
                         Domain model
```

Stage 3 will replace the in-memory storage with JPA/PostgreSQL.

A particularly useful architectural observation is that neither the REST API nor the Thymeleaf UI should need to know whether `AccountRepository` ultimately stores data in a Java `Map` or PostgreSQL.

The intended progression remains:

```text
Stage 1 — Plain Java domain model
        COMPLETE
            ↓
Stage 2 — Spring Boot + REST
        COMPLETE
            ↓
Stage 2.5 — Minimal Spring MVC + Thymeleaf UI
            ↓
Stage 3 — JPA + PostgreSQL
            ↓
Stage 4 — Microservices + Kafka
            ↓
Stage 5 — Docker + AWS
```

Do not expand Stage 2.5 into a frontend project after these requirements are satisfied. Its purpose is to provide a small usable UI while reinforcing Spring MVC concepts before persistence work begins.
