# EHB Stage 2.5 — Minimal HTML/JavaScript UI

## Ticket Pack

**Project:** Edinburgh Hanoverian Bank (EHB) Online Banking  
**Stage:** 2.5 — Minimal browser UI  
**Starting point:** Stage 2 Spring Boot + REST complete; storage remains in memory  
**Technology constraint:** Plain HTML + vanilla JavaScript only. **No CSS.**

---

## 1. Purpose of this stage

Stage 2.5 adds a deliberately tiny browser interface to the existing Spring Boot REST application before persistence is introduced.

The objective is **not** to learn frontend development or to make EHB attractive. The objective is to make the existing API usable from a browser and to reinforce how a browser client communicates with a REST backend.

The UI should ultimately allow the user to perform everything currently available through the API:

- check application status;
- browse the seeded businesses;
- browse the seeded accounts;
- view an account and its current balance;
- view an account's transaction history;
- deposit money into an account;
- withdraw money from an account;
- make a payment between accounts;
- view a business and the accounts belonging to it.

Unlike a purely mechanical UI, the user should not normally need to know account numbers or changing business UUIDs beforehand. Accounts and businesses should be presented through ordinary HTML selection controls where practical.

---

## 2. Hard scope limits

These constraints are part of the exercise.

Do **not** introduce:

- CSS;
- Thymeleaf;
- React, Vue, Angular, Svelte, or another frontend framework;
- npm or Node.js;
- Bootstrap or another UI toolkit;
- a frontend build system;
- authentication or login;
- Spring Security;
- new banking features simply to make the UI more impressive;
- database persistence;
- JPA or PostgreSQL.

The expected frontend structure is approximately:

```text
src/main/resources/static/
    index.html
    app.js
```

Spring Boot should serve these files directly.

The browser should communicate with the **existing REST API** using JavaScript `fetch()` calls. Do not bypass the controllers by introducing a second UI-specific route into the service or repository layer.

---

## 3. Learning approach for these tickets

These tickets are intentionally more guided than a normal professional ticket because this is a learning project and browser JavaScript is not the main subject of the wider curriculum.

For each ticket:

1. read the objective and background first;
2. locate the relevant existing code before editing anything;
3. implement the requirement yourself;
4. use the hints only when needed;
5. verify the behaviour manually;
6. run the existing automated tests after backend changes;
7. commit a coherent completed ticket before moving on where practical.

Do not ask an AI tool to generate the whole UI in one operation. The value of this short stage is understanding the connection:

```text
HTML control
    ↓
JavaScript event
    ↓
fetch()
    ↓
HTTP request
    ↓
Spring controller
    ↓
service/domain/repository
    ↓
JSON response
    ↓
JavaScript
    ↓
HTML updated
```

---

# EHB Ticket UI-01 — Expose All Accounts Through the REST API

## Objective

Add a REST endpoint that returns all accounts currently known to the application.

The intended endpoint is:

```text
GET /api/accounts
```

It should return a JSON array of `AccountResponse` objects.

## Why this ticket exists

The existing UI-independent API can retrieve an account when its account number is already known. For the browser UI, however, we want to populate an account selection list automatically.

This is also a useful small REST exercise before moving into JavaScript.

## Existing code to inspect

Before writing code, inspect:

- `AccountController`;
- `AccountService`;
- `AccountRepository`;
- `AccountResponse`.

In particular, notice that `AccountRepository` already has access to the collection of accounts and already exposes `getAccounts()`.

## Requirements

1. `GET /api/accounts` returns every seeded account.
2. The controller returns response DTOs rather than serialising `Account` domain objects directly.
3. Keep the usual layering:

```text
Controller → Service → Repository
```

4. Do not put repository access directly in the controller.
5. Do not alter the existing single-account endpoint.

## Concepts being practised

- collection-returning REST endpoints;
- controller/service/repository responsibilities;
- mapping domain objects to DTOs;
- `List` transformations;
- reusing `AccountResponse.from(...)`.

## Starting hint

Work from the bottom upward.

Ask yourself:

- What collection can the repository already provide?
- What method could the service expose to the controller?
- How can a `List<Account>` become a `List<AccountResponse>`?

This may be a natural place to use a Stream, but use one only if you understand what the individual operations are doing.

## Verification

Start the application and request:

```text
GET http://localhost:8080/api/accounts
```

You should receive a JSON array containing the seeded accounts such as `1111`, `2222`, `3333`, `4444`, and `5555`.

Run:

```bash
mvn test
```

before considering the ticket complete.

---

# EHB Ticket UI-02 — Expose All Businesses Through the REST API

## Objective

Add a REST endpoint that returns all seeded businesses.

The intended endpoint is:

```text
GET /api/businesses
```

It should return a JSON array of `BusinessResponse` objects.

## Why this ticket exists

Business IDs are generated UUIDs and therefore change each time the application starts. Requiring a browser user to copy a UUID from the Spring console would defeat much of the point of adding a simple selectable UI.

## Existing code to inspect

Inspect:

- `BusinessController`;
- `BusinessService`;
- `BusinessRepository`;
- `BusinessResponse`.

Compare the structure with the solution you wrote for UI-01 rather than treating this as a completely new problem.

## Requirements

1. `GET /api/businesses` returns all seeded businesses.
2. Return `BusinessResponse` DTOs.
3. The `password` field must **not** appear in the JSON.
4. Preserve the controller → service → repository layering.
5. Existing business lookup endpoints must continue working.

## Concepts being practised

- reinforcing REST collection endpoints;
- DTO boundaries;
- avoiding accidental exposure of domain state;
- recognising repeated architectural patterns.

## Verification

Use Postman or the browser to request:

```text
GET http://localhost:8080/api/businesses
```

Confirm that:

- all seeded businesses appear;
- each has its generated ID;
- account information is present according to `BusinessResponse`;
- no password appears.

Run the test suite again.

---

# EHB Ticket UI-03 — Create the Static HTML Page

## Objective

Create the first browser page for EHB and confirm that Spring Boot serves it.

Do **not** add functional JavaScript yet.

## Background

Spring Boot automatically serves static resources placed in its standard static-resource location. For this project we will use:

```text
src/main/resources/static/
```

The main page should be named:

```text
index.html
```

## Requirements

Create a valid HTML document containing, at minimum:

- an EHB Online Banking heading;
- an application-status area;
- a business section;
- an account section;
- a transaction-history section;
- a deposit section;
- a withdrawal section;
- a payment section.

At this stage these can simply contain headings, labels, empty `<select>` elements, inputs and buttons.

**Do not add CSS.**

Do not worry about making the page attractive.

## Suggested controls

You will probably need HTML elements corresponding roughly to:

```text
Business: [select]
Account:  [select]

[View Account]
[View Transactions]

Deposit amount:  [input] [Deposit]
Withdraw amount: [input] [Withdraw]

Payment from: [select]
Payment to:   [select]
Amount:       [input]
[Make Payment]
```

These are suggestions about page functionality, not a required visual layout.

## Concepts being practised

- basic HTML document structure;
- forms and controls;
- `<select>` / `<option>`;
- `<input>`;
- `<button>`;
- IDs as hooks for JavaScript;
- Spring Boot static resources.

## Important design point

Give interactive elements sensible `id` attributes. JavaScript will later need a reliable way to locate them.

For example, understand the relationship between:

```html
<button id="someButton">Do something</button>
```

and JavaScript that later retrieves the element by that ID.

You do not need to use that exact ID.

## Verification

Start Spring Boot and visit:

```text
http://localhost:8080/
```

The page should appear.

It will be plain and visually unimpressive. That is correct.

The existing `/api/...` endpoints should continue working unchanged.

---

# EHB Ticket UI-04 — Connect a JavaScript File and Check Application Status

## Objective

Create `app.js`, load it from `index.html`, and make the first browser-to-API request.

Use the existing:

```text
GET /api/status
```

endpoint and display its result on the page.

## Why start here?

The status endpoint is the simplest API endpoint in the application. It returns plain text and requires no IDs, JSON request body, or user input.

It therefore gives us the smallest possible introduction to `fetch()`.

## Requirements

1. Create:

```text
src/main/resources/static/app.js
```

2. Load it from `index.html` with a `<script>` element.
3. When the page loads, request `/api/status`.
4. Read the response as text.
5. Put that text into the status area of the HTML page.
6. If the request fails, display a simple failure message rather than silently doing nothing.

## Concepts being practised

- external JavaScript files;
- browser execution of JavaScript;
- `fetch()`;
- HTTP GET requests;
- Promises / `async` and `await` at an introductory level;
- `response.text()`;
- DOM lookup;
- changing `textContent`.

## Guidance

Before implementing the complete request, make sure you understand these separately:

```javascript
document.getElementById(...)
```

and:

```javascript
fetch(...)
```

If you use `async` / `await`, be able to explain why the function needs to be `async` and what `await` is waiting for.

## Verification

Reload the page with Spring Boot running.

The status returned by the API should now appear somewhere on the HTML page without you manually making a Postman request.

Open the browser developer console if something goes wrong. Begin getting comfortable with using it to see JavaScript errors.

---

# EHB Ticket UI-05 — Load Accounts Into Selection Lists

## Objective

Use JavaScript to call:

```text
GET /api/accounts
```

and populate the account selection controls from the returned JSON.

## Requirements

When the page loads:

1. fetch the account collection;
2. parse the JSON response;
3. create an `<option>` for each account;
4. add the options to the relevant `<select>` controls.

At minimum, account choices are needed for:

- viewing an account;
- the payment source account;
- the payment target account.

It is acceptable to populate several selects from the same returned array.

## What should an option contain?

The actual submitted/selected value should make it easy to obtain the account number later.

The visible label should be useful to a human. For example, it might combine account number and currency.

Do not spend time trying to make the labels beautiful.

## Concepts being practised

- `response.json()`;
- JavaScript arrays;
- iteration;
- creating DOM elements;
- setting element properties;
- appending elements to the page;
- separating machine values from human-readable labels.

## Important question to understand

When the API returns:

```text
JSON array → JavaScript array
```

what does one element of that JavaScript array look like, and how do its property names relate to the fields of `AccountResponse`?

Use the browser console to inspect the data if useful.

## Verification

Reload the page.

The account dropdowns should populate automatically from the backend. Do not hard-code `1111`, `2222`, etc. into the HTML.

Restarting the application should still produce the lists from the API.

---

# EHB Ticket UI-06 — Load Businesses and Display a Selected Business

## Objective

Populate the business selector from:

```text
GET /api/businesses
```

and allow the user to view the selected business's information.

## Requirements

1. Fetch the business list when the page loads.
2. Populate a `<select>` with the returned businesses.
3. Use the business ID as the value associated with the selection.
4. Display at least:
   - business name;
   - business ID;
   - accounts belonging to that business.
5. Do not expose or attempt to use the password field.

You may either display information already contained in the selected `BusinessResponse` or exercise the existing individual-business/business-account endpoint after selection. Prefer the approach that helps you understand the API rather than adding unnecessary complexity.

## Concepts being practised

- using UUID strings returned by a server;
- selecting objects by ID;
- displaying structured JSON data;
- connecting an HTML event to JavaScript behaviour.

## Verification

Select each business in turn and confirm that the displayed name/accounts correspond to the selected business.

Restart Spring Boot. The UUIDs will change. The UI should still work because it obtains them from the API rather than hard-coding them.

---

# EHB Ticket UI-07 — Display Account Details

## Objective

Allow the user to select an account and retrieve its current details using the existing single-account endpoint.

The relevant API shape is:

```text
GET /api/accounts/{accountNumber}
```

## Requirements

1. The user selects an account from the populated account selector.
2. An action such as a button click triggers the request.
3. JavaScript inserts the selected account number into the request URL.
4. Display at least:
   - account number;
   - currency;
   - current balance.
5. Do not simply display the copy of the account object that was obtained when populating the dropdown. Make the individual account request so that this ticket exercises the endpoint and retrieves current state.

## Concepts being practised

- event listeners;
- reading the current value of a `<select>`;
- constructing a URL using a value;
- displaying JSON properties;
- understanding why current server state should be fetched rather than assuming an old browser copy is current.

## Verification

View several different accounts.

Confirm that the information displayed corresponds to the API response you would previously have inspected in Postman.

---

# EHB Ticket UI-08 — Display Transaction History

## Objective

Allow the user to display transaction history for the selected account.

Use:

```text
GET /api/accounts/{accountNumber}/transactions
```

## Requirements

1. Obtain the selected account number.
2. Fetch its transaction history.
3. Display all returned transactions on the page.
4. Show useful fields such as:
   - transaction type;
   - amount;
   - timestamp;
   - identifier if useful.
5. If the returned array is empty, display a simple message such as `No transactions` rather than appearing broken.

## Display choice

A plain HTML table is appropriate:

```text
Type       Amount       Time
INCOMING   500.00       ...
OUTGOING   100.00       ...
```

Do not style the table.

## Concepts being practised

- rendering arrays of server data;
- loops / iteration;
- creating repeated DOM elements;
- clearing old results before displaying new ones;
- distinguishing an empty successful response from an error.

## Verification

Inspect an account with seeded transactions and one without transactions if available.

Later tickets will let you verify that this display changes after deposits, withdrawals and payments.

---

# EHB Ticket UI-09 — Deposit Money From the Browser

## Objective

Allow a user to deposit an amount into the selected account using the existing deposit endpoint.

Relevant API shape:

```text
POST /api/accounts/{accountNumber}/deposit
```

## Before coding

Review `DepositRequest` and the existing controller method. Identify the exact JSON property name the backend expects.

The frontend should conform to the existing API; do not alter the API merely because another property name would be easier to remember in JavaScript.

## Requirements

1. Read the selected account number.
2. Read the amount entered into the deposit input.
3. Construct the JavaScript object corresponding to `DepositRequest`.
4. Convert it to JSON.
5. send a POST request with the appropriate `Content-Type` header;
6. parse the returned transaction response;
7. display a simple success result;
8. refresh/re-fetch the account details so the new balance becomes visible.

## Concepts being practised

- HTTP POST from JavaScript;
- request headers;
- `JSON.stringify()`;
- relationship between a JavaScript object, JSON, Jackson and a Java request DTO;
- updating UI after server-side state changes.

## Important conceptual path

Be able to explain this after completing the ticket:

```text
JavaScript object
    ↓ JSON.stringify()
JSON request body
    ↓ HTTP
Jackson
    ↓
DepositRequest
    ↓
AccountController
```

## Verification

Choose an account, note its balance, deposit a small fictional amount, and confirm:

- a successful response is displayed;
- the balance increases;
- transaction history includes the new transaction.

Do not add persistence. The change disappearing after an application restart is still expected at this stage.

---

# EHB Ticket UI-10 — Withdraw Money From the Browser

## Objective

Implement withdrawal through the UI using:

```text
POST /api/accounts/{accountNumber}/withdraw
```

## Requirements

Follow the same general browser/API pattern as the deposit ticket, but use the existing `WithdrawRequest` contract.

After a successful withdrawal:

- show a simple success result;
- refresh the displayed account balance;
- transaction history should reflect the operation when viewed/refreshed.

## Why this is a separate ticket

Much of the structure should now look familiar. The learning goal is to recognise and reuse a pattern rather than treating every HTTP request as a new technology.

Before implementing it, compare the deposit and withdrawal API methods and identify what is genuinely different.

## Concepts being practised

- pattern recognition;
- reuse without premature abstraction;
- another POST/JSON request;
- server validation and errors.

## Verification

Test both:

1. a valid withdrawal;
2. a withdrawal larger than the available balance.

The valid case should update the balance. The failure should leave the balance unchanged.

Do not yet spend significant time making error messages elegant; dedicated error handling comes later.

---

# EHB Ticket UI-11 — Make a Payment Between Accounts

## Objective

Allow the user to make a payment between two accounts through the browser.

Use the existing:

```text
POST /api/payments
```

endpoint.

## Requirements

The UI should provide:

- a source-account selector;
- a target-account selector;
- an amount input;
- a payment button.

On submission:

1. read the source account number;
2. read the target account number;
3. read the amount;
4. construct the object expected by `PaymentRequest`;
5. POST it as JSON;
6. display useful information from `PaymentResponse`;
7. refresh any currently displayed account details that may now be stale.

## Existing code to review first

Review:

- `PaymentRequest`;
- `PaymentResponse`;
- `PaymentController`;
- the manual Postman request you previously used for a successful payment, if available.

## Concepts being practised

- slightly richer JSON request bodies;
- multiple UI inputs contributing to one request;
- DTO contracts;
- observing one operation changing multiple domain objects;
- keeping displayed client state in sync with backend state.

## Verification

Use compatible source and target accounts and a small fictional amount.

Confirm:

- the payment response reports the expected completed state;
- the source balance decreases;
- the target balance increases;
- transaction histories contain corresponding outgoing/incoming transactions.

Also try at least one failure case already enforced by the backend, such as insufficient funds or an invalid currency combination.

The browser must not reproduce these banking rules itself. The backend remains responsible for them.

---

# EHB Ticket UI-12 — Add Simple Shared Success and Error Feedback

## Objective

Make the UI understandable when an API request succeeds or fails without building a sophisticated notification system.

## Background

The backend currently has two error-response shapes:

- domain exceptions usually return `ErrorResponse` with a `message` field;
- Bean Validation failures and malformed JSON currently use Spring Boot's default error response.

Stage 2 deliberately left these shapes inconsistent. **Do not redesign backend exception handling as part of this UI ticket.**

## Requirements

Create a simple area of the page for feedback.

For operations such as deposit, withdrawal and payment:

- display a short success message after success;
- display a useful error message after a non-success HTTP response;
- do not allow a failed request to produce an uncaught JavaScript error that leaves the user with no indication of what happened.

Keep the implementation simple enough to tolerate the two existing backend error shapes.

## Important HTTP concept

Investigate this behaviour carefully:

> Does `fetch()` automatically throw an exception merely because the server returned HTTP 400, 404 or 409?

The answer matters to how you write error handling.

Pay attention to:

```javascript
response.ok
```

and understand what it represents.

## Concepts being practised

- HTTP success vs failure status codes;
- `response.ok`;
- JavaScript `try` / `catch`;
- API error bodies;
- defensive client behaviour.

## Scope warning

Do not build:

- toast notifications;
- modal dialogs;
- colour-coded messages;
- custom error classes;
- elaborate generic frontend infrastructure.

A plain text message is enough.

## Verification

From the UI, deliberately trigger several outcomes:

- successful deposit;
- non-positive amount;
- withdrawal exceeding the balance;
- payment failure.

Each should leave the page usable and provide some indication of what happened.

---

# EHB Ticket UI-13 — Reduce Obvious JavaScript Duplication

## Objective

Review `app.js` now that all major features work and remove only the most obvious duplication.

This is a small refactoring ticket, not an invitation to create a frontend framework.

## What to look for

There may now be repeated operations such as:

- fetching JSON;
- checking `response.ok`;
- obtaining selected account numbers;
- refreshing account information;
- displaying messages;
- populating account selectors.

Identify repeated code that genuinely becomes easier to understand when extracted into a small named function.

## Requirements

1. Behaviour must remain unchanged.
2. Prefer small functions with clear names.
3. Do not introduce classes merely to organise a small script.
4. Do not introduce modules/build tooling.
5. Do not attempt to remove every repeated line.

## Concepts being practised

- refactoring after functionality exists;
- functions and parameters in JavaScript;
- DRY as a guideline rather than an absolute rule;
- readability vs abstraction.

## Reflection question

For every helper function you create, be able to answer:

> Is this function making the code easier to understand, or am I abstracting simply because two pieces of code look similar?

## Verification

Repeat the core UI operations after refactoring and confirm they still work.

---

# EHB Ticket UI-14 — Stage 2.5 End-to-End Checkpoint

## Objective

Verify the complete browser → REST API → application → browser flow before beginning JPA/PostgreSQL.

Do not add new features during this ticket unless verification reveals a genuine bug in Stage 2.5 requirements.

## Preparation

Start from a fresh application restart so that the known in-memory seed state is restored.

Run:

```bash
mvn test
```

The existing Java/Spring tests must remain green.

## Browser verification scenario

From the browser UI only, without using Postman for the main scenario:

1. confirm application status is displayed;
2. confirm account selectors populate automatically;
3. confirm business selectors populate automatically;
4. view a business and its accounts;
5. view account `1111` and note its starting balance;
6. view its transaction history;
7. make a deposit and confirm the balance/history changes;
8. make a valid withdrawal and confirm the balance/history changes;
9. make a valid payment between compatible accounts;
10. confirm both source and target balances change appropriately;
11. confirm corresponding transaction histories change;
12. deliberately trigger an insufficient-funds failure and confirm the UI reports it without corrupting account state.

## Architecture checkpoint

At the end of this ticket, be able to explain the complete path for one operation such as deposit:

```text
User enters amount in HTML
        ↓
JavaScript handles button/event
        ↓
JavaScript constructs request data
        ↓
fetch() sends HTTP POST + JSON
        ↓
Tomcat receives HTTP request
        ↓
Spring MVC routes it to AccountController
        ↓
Jackson creates DepositRequest
        ↓
Bean Validation checks request
        ↓
AccountController calls AccountService
        ↓
AccountService obtains Account through repository
        ↓
Account domain method applies business rules
        ↓
TransactionResponse returned
        ↓
Jackson serialises response as JSON
        ↓
fetch() receives Response
        ↓
JavaScript parses JSON
        ↓
HTML is updated
```

You do not need to memorise every framework class involved, but you should understand the responsibilities and direction of travel.

## Completion criteria

Stage 2.5 is complete when:

- all existing Stage 2 capabilities can be exercised through the browser;
- businesses/accounts are discovered from the API rather than hard-coded into HTML;
- no CSS or frontend framework has been introduced;
- no database has been introduced;
- the existing test suite remains green;
- API/domain rules remain on the backend;
- the UI is crude but usable;
- you can explain how JavaScript `fetch()` communicates with the Spring REST controllers.

Commit the completed stage before starting persistence.

A suitable commit message could describe the feature rather than the learning exercise, for example:

```text
Add minimal browser UI for banking operations
```

---

# 4. Stage 2.5 outcome and transition to Stage 3

At completion, the application architecture should conceptually be:

```text
Browser
HTML + vanilla JavaScript
        ↓ HTTP / JSON
Spring Boot REST controllers
        ↓
Services
        ↓
In-memory repositories
        ↓
Domain model
```

Stage 3 will replace the in-memory persistence mechanism with JPA/PostgreSQL.

A key architectural observation to carry into Stage 3 is that the browser should **not need to know** whether account data is held in a `Map` or in PostgreSQL. If the REST contracts remain stable, the frontend can continue making the same requests.

That will make the value of the existing layers concrete:

```text
UI → API → Service → Repository abstraction → storage
```

The next major learning stage remains:

```text
Stage 3 — JPA + PostgreSQL + persistence/testing
```

Do not expand Stage 2.5 further merely because a browser UI now exists. Its purpose has been fulfilled once it provides a thin usable client over the REST API.
