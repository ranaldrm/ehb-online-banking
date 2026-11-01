# EHB Stage 2.5 — Minimal Thymeleaf UI

## Ticket Pack (Beginner-Friendly Edition)

**Project:** Edinburgh Hanoverian Bank (EHB) Online Banking  
**Stage:** 2.5 — Minimal server-rendered browser UI  
**Starting point:** Stage 2 Spring Boot + REST complete; storage remains in memory  
**UI technology:** Spring MVC + Thymeleaf + plain HTML  
**Technology constraint:** **No CSS and no JavaScript unless a later requirement genuinely makes a tiny amount unavoidable.**

> **If you have never used Thymeleaf before, start here.** This pack assumes no
> prior frontend/template experience. Every ticket has a *"Background for
> beginners"* box that teaches the new concept in plain language, and a *worked
> example* you can study. The worked examples deliberately use a **different,
> made-up scenario** (products, orders, warehouses) rather than the real EHB
> accounts — that way you learn the pattern but still write the actual banking
> code yourself, which is the whole point of this learning project.

---

## 1. Purpose of this stage

Stage 2.5 adds a deliberately small browser interface to the existing Spring Boot application before persistence is introduced.

An earlier Stage 2.5 plan used plain HTML plus JavaScript `fetch()` calls. That approach has been replaced: learning JavaScript from scratch would be a large detour from the main purpose of EHB, which is learning Java, Spring Boot, backend development, persistence, and related enterprise technologies.

The revised UI uses **Spring MVC + Thymeleaf** so that building the UI reinforces Spring itself rather than introducing a separate frontend programming curriculum.

The UI should ultimately allow the user to:

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

## 4. What you already have (read this before TH-01)

This stage is being started on a codebase that is further along than a blank
project. A few facts that the tickets rely on:

- **Thymeleaf is already on the classpath.** `pom.xml` already contains
  `spring-boot-starter-thymeleaf`, so TH-01 is about *verifying and
  understanding* it, not adding it.
- **A `UIController` already exists** at
  `src/main/java/com/ehb/banking/controller/UIController.java`, annotated
  `@Controller`, mapping `GET /` to the view `bankhome`. (Watch its `Model`
  import — see TH-02/TH-03.)
- **A `bankhome.html` template already exists** under
  `src/main/resources/templates/`, currently containing only a bare heading.
- **Seeded accounts** (stable account numbers): `1111` GBP £500, `2222` EUR 0,
  `3333` USD 0, `4444` GBP £500, `5555` EUR 0.
- **Seeded businesses** with **random UUID IDs** (they change every restart):
  `Dodgy Dave's` (accounts 1111, 2222), `University of Brigadoon` (3333, 4444),
  `workShy Consultants LLC` (5555).
- **The services you will call:**
  `AccountService` → `getAllAccounts()`, `getAccountByNumber(String)`,
  `getTransactionHistory(String)`, `deposit(String, BigDecimal)`,
  `withdraw(String, BigDecimal)`;
  `BusinessService` → `getAllBusinesses()`, `getBusinessByID(String)`,
  `getAccountsForBusiness(String)`;
  `PaymentService` → `processPayment(String source, String target, BigDecimal amount)`.
- **The display records (and their exact field names):**
  `AccountResponse{ accountNumber, currency, balance }`;
  `TransactionResponse{ identifier, transactionType, transactionAmount, timestamp }`
  (note `transactionAmount`, not `amount`);
  `BusinessResponse{ businessID, businessName, accounts }`.
- **Error handling:** `GlobalExceptionHandler` is a `@RestControllerAdvice`, so
  it produces JSON. It will **not** render HTML error pages for the UI — TH-10
  addresses that separately.

The UI controller may call the existing **service layer directly**:

```text
UI Controller  → Service
REST Controller → Service
```

Both controllers are presentation-layer adapters over the same application
services. Do **not** make the server send HTTP requests to its own REST API just
to reuse an endpoint.

---

## 5. Learning approach

These tickets are intentionally guided, but they are **not** finished solutions.
For each ticket:

1. read the *Background for beginners* box;
2. study the *worked example* (which uses a different, made-up scenario);
3. inspect the named existing EHB code;
4. implement the real EHB version yourself;
5. manually verify the browser behaviour;
6. run `mvn test` after meaningful backend changes;
7. keep the UI ugly and small.

The aim is not to become a Thymeleaf specialist. The aim is to learn a useful
slice of Spring MVC while obtaining a convenient UI.

---

# EHB Ticket TH-01 — Get Ready for Thymeleaf

## Objective

Make sure the project can serve server-rendered HTML pages with Thymeleaf, and
confirm the application still builds and all existing tests pass.

## Background for beginners

**What is a server-rendered page?** So far EHB only speaks JSON: a tool like
Postman sends a request and a `@RestController` returns raw data. A human in a
browser wants a *web page* (HTML) instead. "Server-rendered" means the Java
server builds the finished HTML itself and sends the whole document; the browser
just displays it. No JavaScript, no second API call.

**What is Thymeleaf?** A template engine. A template is an HTML file with a few
special `th:` attributes. On each request Spring hands your data to Thymeleaf,
which fills the gaps and produces plain HTML:

```text
template (HTML with gaps)  +  Java data  =  finished HTML
```

**What is a starter?** A single Maven dependency bundling version-matched
libraries. `spring-boot-starter-web` serves HTTP + REST;
`spring-boot-starter-thymeleaf` adds the engine plus the glue so Spring MVC uses
it automatically. That automatic wiring is **auto-configuration**: when Boot
sees Thymeleaf on the classpath at startup it creates the view resolver for you.

## Steps

1. **Inspect first:** `pom.xml`, `EhbBankingApplication`, and
   `src/main/resources`.
2. **Check for the starter.** Search `pom.xml` for:

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-thymeleaf</artifactId>
   </dependency>
   ```

   In this project it is **probably already present** — confirm it, and confirm
   `spring-boot-starter-web` is still there too. Only add the block (no
   `<version>`) if it is genuinely missing.
3. **Do not touch the REST controllers.**
4. **Verify:**

   ```bash
   mvn test
   mvn spring-boot:run
   ```

   Then confirm `GET http://localhost:8080/api/status` still returns
   `Hello, from Edinburgh Hanoverian Bank`. Do not build any UI yet.

## Questions you should be able to answer

- What does the Thymeleaf starter give you?
- Why don't you build a template engine by hand?
- Why do the `/api/...` endpoints keep working?

## Done when

Both `spring-boot-starter-web` and `spring-boot-starter-thymeleaf` are present,
`mvn test` is green, and `/api/status` still works.

---

# EHB Ticket TH-02 — Render Your First Thymeleaf Page

## Objective

Create the simplest possible server-rendered EHB page: HTML with a heading,
served by a `@Controller`. No banking data yet.

## Background for beginners

**`@RestController` vs `@Controller`.** On a `@RestController`, returning a
`String` sends that exact text as the response body. On a plain `@Controller`,
returning a `String` names a **view** (a template to render):

```text
@RestController + return "x"  →  client receives the text "x"
@Controller     + return "x"  →  client receives templates/x.html rendered as HTML
```

**Where templates live:** `src/main/resources/templates/`. The view name
`"bankhome"` maps to `templates/bankhome.html`.

## Worked example (a different page, so you still write the EHB one)

```java
@Controller
public class GreetingController {
    @GetMapping("/hello")
    public String hello() {
        return "greeting"; // -> templates/greeting.html
    }
}
```

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Greeting</title></head>
<body>
    <h1>Hello there</h1>
</body>
</html>
```

The `xmlns:th` namespace is what makes `th:...` attributes work later; add it now.

## Your task

1. Read the existing `UIController` (`@Controller`, `GET /` → `bankhome`).
2. Open the existing `bankhome.html` (currently just a bare `<h1>`).
3. Flesh it into a full document: `<!DOCTYPE html>`, an
   `<html xmlns:th="http://www.thymeleaf.org">` root, a `<head><title>`, and a
   `<body>` with `<h1>EHB Online Banking</h1>`. No CSS, no JavaScript.

> **Likely bug to watch for.** Check `UIController`'s `Model` import. The correct
> type is `org.springframework.ui.Model`; an IDE may have imported a different
> `Model` (e.g. from a logging library). You don't use `Model` yet, but you will
> in TH-03 — fix it to `org.springframework.ui.Model` (or remove the unused
> import and re-add the right one in TH-03).

## Verify

Visit `http://localhost:8080/`; see the heading. "View page source" should show
plain HTML with no `th:` attributes. Confirm `/api/status` still responds.

## Done when

`/` shows an HTML page with the heading, the source is plain HTML, and REST is
unaffected.

---

# EHB Ticket TH-03 — Pass Data From Java Into the Page

## Objective

Send one simple value from the controller into the page and display it with
Thymeleaf. Just one string, to focus on the mechanism.

## Background for beginners

**The `Model` is a box of data for the page.** You add values by name:

```java
model.addAttribute("pageStatus", "All systems go");
```

**`th:text` writes a value into an element:**

```html
<p th:text="${pageStatus}">placeholder text</p>
```

`${pageStatus}` reads the model attribute; `th:text` replaces the element's text
with it at render time. The placeholder text only shows if you open the raw file
directly — so you are not hard-coding the real value.

**Lifecycle:** controller puts data in the Model → Spring hands it to Thymeleaf →
Thymeleaf fills the template → browser receives finished HTML. (Contrast the old
JS plan, where the browser loaded an empty page and *then* fetched data.)

## Worked example

```java
@GetMapping("/hello")
public String hello(Model model) {
    model.addAttribute("greeting", "Hello there");
    return "greeting";
}
```

```html
<h1 th:text="${greeting}">placeholder heading</h1>
```

## Your task

1. In `UIController.bankHome`, confirm the `Model` import is
   `org.springframework.ui.Model` (fix if not).
2. Add a simple attribute (e.g. a status message) to the model.
3. Display it in `bankhome.html` with `th:text` — do not type the literal text
   into the HTML.

## Verify

The page shows the value. Change the value **in Java**, restart, reload — the
page must reflect the change.

## Reflection

How does this differ from the old JavaScript approach (requests, where logic
lives, what the browser must do)?

## Done when

The page shows a Java-originated value, changing the Java value changes the page,
and nothing is hard-coded in the HTML.

---

# EHB Ticket TH-04 — Display All Accounts in a Table

## Objective

Show every seeded account on a page, in a plain HTML table, using real data from
`AccountService`.

## Background for beginners

**Go through the service, not the repository:**

```text
UIController → AccountService → AccountRepository
```

Use the existing `List<Account> getAllAccounts()`. Map each `Account` to an
`AccountResponse` (there is `AccountResponse.from(account)`), just like the REST
side. `AccountResponse` fields: `accountNumber`, `currency`, `balance`.

**`th:each` — the "for each" loop:**

```html
<tr th:each="item : ${items}">
    <td th:text="${item.name}">name</td>
</tr>
```

"For each `item` in `items`, produce one `<tr>`." For a record,
`${account.accountNumber}` calls the generated accessor automatically.

## Worked example (a products table)

```java
@GetMapping("/products")
public String products(Model model) {
    model.addAttribute("products",
        productService.getAll().stream().map(ProductResponse::from).toList());
    return "products";
}
```

```html
<table>
    <tr><th>Name</th><th>Price</th></tr>
    <tr th:each="product : ${products}">
        <td th:text="${product.name}">name</td>
        <td th:text="${product.price}">price</td>
    </tr>
</table>
```

The header row has no `th:each` (shown once); the data row repeats.

## Your task

1. Read `AccountService.getAllAccounts()`, `AccountResponse`, and
   `AccountController.getAllAccounts()`.
2. In `UIController`, fetch and map accounts to `AccountResponse`, add the list
   to the model.
3. Render one row per account with `th:each`, showing account number, currency,
   balance. A plain table/list is fine; no CSS. Do not inject
   `AccountRepository` into the controller.

## Verify

All **five** accounts appear (1111 GBP 500, 2222 EUR 0, 3333 USD 0, 4444 GBP 500,
5555 EUR 0). Restart and reload — the list reappears, proving it is read live.

## Done when

All five accounts render via the service, nothing hard-coded.

---

# EHB Ticket TH-05 — Click an Account to See Its Details

## Objective

Make each listed account a link to a per-account details page at `/accounts/1111`.

## Background for beginners

**Dynamic URLs + `@PathVariable`:** the changing part of the URL is captured by a
method parameter:

```java
@GetMapping("/accounts/{accountNumber}")
public String accountDetails(@PathVariable String accountNumber, Model model) { ... }
```

Same tool your REST controller uses — the only difference is this returns a
**view name**, not a DTO.

**`th:href` builds links from data** with the `@{...}` URL syntax:

```html
<a th:href="@{/accounts/{num}(num=${account.accountNumber})}">View</a>
```

For account `1111` this renders `href="/accounts/1111"`.

**Looking one up:** `AccountService.getAccountByNumber(String)` (throws
`AccountNotFoundException` if missing).

## Worked example (product details)

```html
<a th:href="@{/products/{id}(id=${product.id})}">View</a>
```

```java
@GetMapping("/products/{id}")
public String productDetails(@PathVariable String id, Model model) {
    model.addAttribute("product", ProductResponse.from(productService.getById(id)));
    return "product-details";
}
```

## Your task

1. Add a `th:href` link in each account row to `/accounts/{accountNumber}`.
2. Add a `GET /accounts/{accountNumber}` method with a `@PathVariable`; look up
   via the service, map to `AccountResponse`, add to the model, return a new
   view (e.g. `account-details`).
3. Create `account-details.html` showing account number, currency, balance.

## Verify

Clicking different accounts changes the URL and the shown account to match.
(Account numbers are stable across restarts; business IDs in TH-07 are not.)

## Done when

Each account links to its own working details page; URL and content match.

---

# EHB Ticket TH-06 — Show the Transaction History

## Objective

On the account details page, list that account's transactions; show
"No transactions" when there are none.

## Background for beginners

Use `AccountService.getTransactionHistory(String)` and map each `Transaction` to
`TransactionResponse` (`TransactionResponse.from(...)`).

> **Exact field names** (not what you might guess): `identifier`,
> `transactionType` (`INCOMING`/`OUTGOING`), **`transactionAmount`** (not
> `amount`), `timestamp`. `${t.amount}` will fail.

**`th:if` / `th:unless`** show an element only when a condition is true/false:

```html
<p th:if="${list.isEmpty()}">Nothing here</p>
<table th:unless="${list.isEmpty()}"> ... </table>
```

## Worked example (order history)

```html
<p th:if="${orders.isEmpty()}">No orders</p>
<table th:unless="${orders.isEmpty()}">
    <tr><th>Type</th><th>Amount</th><th>When</th></tr>
    <tr th:each="order : ${orders}">
        <td th:text="${order.type}">type</td>
        <td th:text="${order.total}">total</td>
        <td th:text="${order.placedAt}">when</td>
    </tr>
</table>
```

## Your task

1. In the account details method, also fetch and map the transaction history and
   add it to the model.
2. In `account-details.html`, show "No transactions" if empty, otherwise loop
   with `th:each` showing `transactionType`, `transactionAmount`, `timestamp`.

## Verify

`1111` and `4444` show one `INCOMING` row (the seeded £500). `2222`, `3333`,
`5555` show the empty message. New rows appear after you build the forms.

## Done when

Accounts with history show rows; accounts without show a clear message.

---

# EHB Ticket TH-07 — Browse Businesses and Their Accounts

## Objective

List the seeded businesses; click one to see its details and accounts.

## Background for beginners

Business IDs are **random UUIDs created at startup** (see `BusinessRepository`) —
they change every restart, so you cannot hard-code them. The UI must list
businesses and carry the current ID in each link (the TH-04/TH-05 pattern).

Services: `getAllBusinesses()`, `getBusinessByID(String)`,
`getAccountsForBusiness(String)`. Map via `BusinessResponse.from(business)`;
fields `businessID`, `businessName`, `accounts` (a `List<AccountResponse>`).

> **Security:** a `Business` has a plaintext `password`. `BusinessResponse`
> omits it on purpose — never put it in the model or template.
>
> **Architecture:** inject `BusinessService`, not `BusinessRepository`.

## Worked example (departments)

```html
<li th:each="dept : ${departments}">
    <a th:href="@{/departments/{id}(id=${dept.id})}" th:text="${dept.name}">name</a>
</li>
```

## Your task

1. Read `BusinessService`, `BusinessResponse`, `BusinessController`.
2. `GET /businesses` lists business names, each linking to `/businesses/{id}`
   using the current `businessID`.
3. `GET /businesses/{id}` shows name, ID, and its accounts (looped). Never show
   the password.

## Verify

`/businesses` shows the three businesses; each links to the right accounts
(Dodgy Dave's → 1111, 2222; University of Brigadoon → 3333, 4444; workShy
Consultants LLC → 5555). **Restart** and repeat: IDs differ but navigation still
works because IDs come from the listing, not from hard-coding.

## Done when

Businesses list and link through correctly, a restart doesn't break it, and the
password is never shown.

---

# EHB Ticket TH-08 — Add a Deposit Form

## Objective

Let the user deposit an amount from the account page via a plain HTML form and a
POST handler. No JavaScript. (This is the biggest conceptual jump — read slowly.)

## Background for beginners

**Forms send a POST:**

```html
<form action="/some/path" method="post">
    <input type="text" name="amount" />
    <button type="submit">Go</button>
</form>
```

Each `<input name="...">` becomes a form parameter in the request body.

**Read parameters with `@RequestParam`** (Spring converts types for you):

```java
@PostMapping("/some/path")
public String handle(@RequestParam BigDecimal amount) { ... }
```

(REST reads JSON via `@RequestBody`; an HTML form sends url-encoded fields read
via `@RequestParam`. Both reach the same service.)

**POST / Redirect / GET — you must use this.** After a successful change, return
a redirect rather than rendering from the POST handler:

```java
return "redirect:/accounts/" + accountNumber;
```

Why: if the POST handler rendered the page, a browser *refresh* would re-send the
POST and deposit again. Redirecting to a GET makes refresh harmless.

**The service call:** `accountService.deposit(accountNumber, amount)` — do not
re-implement deposit rules in the controller.

## Worked example (a restock form)

```html
<form th:action="@{/products/{id}/restock(id=${product.id})}" method="post">
    <input type="text" name="quantity" />
    <button type="submit">Restock</button>
</form>
```

```java
@PostMapping("/products/{id}/restock")
public String restock(@PathVariable String id, @RequestParam int quantity) {
    productService.restock(id, quantity);
    return "redirect:/products/" + id;
}
```

The product/account is identified by the `@PathVariable`; the amount by a
`@RequestParam`.

## Your task

1. Add a deposit `<form method="post">` to `account-details.html`, posting to
   e.g. `/accounts/{accountNumber}/deposit` (build the action with `th:action`
   + `@{...}`), with one amount input.
2. Add a `@PostMapping` handler: account number as `@PathVariable`, amount as
   `@RequestParam BigDecimal`, call `accountService.deposit(...)`, return a
   `redirect:` to the account page.
3. Don't duplicate deposit rules in the controller.

> **Scope:** two `@RequestParam`s are clearer than a form DTO for one field.
> Keep it small.

## Verify

On `1111` (500.00), deposit `50` → back on the page with `550.00` and a new
`INCOMING` row. **Refresh** — it must not deposit again.

## Done when

Deposit updates balance/history, returns you to the GET page, and refresh is safe.

---

# EHB Ticket TH-09 — Add a Withdrawal Form

## Objective

Add a withdrawal form using the exact same pattern as the deposit form.

## Background for beginners

Same shape as TH-08: `form POST → @PostMapping → service → redirect → GET`. Only
the call changes: `accountService.withdraw(accountNumber, amount)`.

The domain owns the rules: non-positive → `NonPositiveAmountException`; over
balance → `ExceedsBalanceException`. Do **not** re-check "enough money?" in the
controller or fake the guard in HTML. A failed withdrawal will throw and produce
an ugly page for now — TH-10 fixes error presentation.

## Worked example

```html
<form th:action="@{/accounts/{num}/withdraw(num=${account.accountNumber})}" method="post">
    <input type="text" name="amount" />
    <button type="submit">Withdraw</button>
</form>
```

```java
@PostMapping("/accounts/{accountNumber}/withdraw")
public String withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
    accountService.withdraw(accountNumber, amount);
    return "redirect:/accounts/" + accountNumber;
}
```

## Your task

Add the withdrawal form and matching handler.

> **Resist abstraction.** Deposit and withdraw look alike — do **not** build a
> shared generic "form processor". Two small clear methods are better here.

## Verify

Valid: `1111` withdraw `100` → `400.00` + an `OUTGOING` row. Invalid: withdraw
`99999` → ugly error for now (note it; TH-10 improves it), balance unchanged.

## Done when

Valid withdrawal updates state and returns to the page; invalid one leaves the
balance intact.

---

# EHB Ticket TH-10 — Show Friendly Form Errors

## Objective

Turn a failed deposit/withdrawal into a short, readable HTML message instead of a
stack trace or JSON.

## Background for beginners

**Why the existing handler won't help:** `GlobalExceptionHandler` is a
`@RestControllerAdvice` → it produces JSON, which is wrong for a browser page. So
the HTML UI needs its own small handling. Do **not** change or remove the REST
`GlobalExceptionHandler`.

**Exceptions to catch** (both extend `BankingException`):
`NonPositiveAmountException`, `ExceedsBalanceException`.

**Recommended approach — catch + flash attribute.** Because of
POST/Redirect/GET, an ordinary model attribute is lost across the redirect; a
**flash attribute** survives exactly one redirect:

```java
@PostMapping("/accounts/{accountNumber}/withdraw")
public String withdraw(@PathVariable String accountNumber,
                       @RequestParam BigDecimal amount,
                       RedirectAttributes redirectAttributes) {
    try {
        accountService.withdraw(accountNumber, amount);
    } catch (BankingException ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
    }
    return "redirect:/accounts/" + accountNumber;
}
```

```html
<p th:if="${error}" th:text="${error}">error message</p>
```

Alternatives: a tiny MVC `@ControllerAdvice` (not `@RestControllerAdvice`), or
catch-and-re-render. Both are more machinery than this UI needs — ask before
going bigger.

## Your task

Make a failed deposit/withdrawal show a short message with the account state
unchanged. Keep rules in the domain; only improve presentation. Don't touch the
REST advice.

## Verify

Non-positive amount (`0`/`-5`) and an over-balance withdrawal both show a
readable message; balance unchanged; a later success doesn't still show the old
error (flash attribute).

## Done when

Both failures show a clear message, state stays valid, REST handling untouched.

---

# EHB Ticket TH-11 — Add the Payment Form

## Objective

A form that moves money between two accounts chosen from drop-downs built from
real account data.

## Background for beginners

**Drop-downs from data:** `<select>` with one `<option>` per account, generated
with `th:each`, value via `th:value`, label via `th:text`:

```html
<select name="source">
    <option th:each="acc : ${accounts}"
            th:value="${acc.accountNumber}"
            th:text="${acc.accountNumber + ' (' + acc.currency + ')'}"></option>
</select>
```

**Service:** `paymentService.processPayment(source, target, amount)`. Don't
re-validate in the controller.

**Contrast:** `JSON → Jackson → PaymentRequest → PaymentService` (REST) vs
`form fields → @RequestParam → PaymentService` (UI). Same service.

## Worked example (transfer-stock form)

```java
@GetMapping("/transfer")
public String transferForm(Model model) {
    model.addAttribute("warehouses",
        warehouseService.getAll().stream().map(WarehouseResponse::from).toList());
    return "transfer";
}

@PostMapping("/transfer")
public String transfer(@RequestParam String from, @RequestParam String to, @RequestParam int quantity) {
    warehouseService.transfer(from, to, quantity);
    return "redirect:/transfer";
}
```

## Your task

1. Read `PaymentService`, `PaymentRequest`, `PaymentResponse`,
   `PaymentController`.
2. A `GET` page shows the form; put the accounts (as `AccountResponse`s) in the
   model for the drop-downs.
3. Form: source `<select>`, target `<select>`, amount input, submit.
4. `@PostMapping` reads the three fields with `@RequestParam`, calls
   `processPayment(...)`, redirects sensibly.
5. Reuse TH-10's error handling for failures.

## Verify

Valid GBP→GBP payment `1111 → 4444` for `50`: source drops, target rises, both
get transactions. A failing payment (currency mismatch `1111`→`2222`, or amount
over balance) is reported without a crash.

## Done when

A valid payment moves money and records transactions on both accounts; a failing
one is reported gracefully.

---

# EHB Ticket TH-12 — Add Simple Navigation (and Tidy Up)

## Objective

Make the UI navigable by links instead of typed URLs. Optionally remove obvious
template duplication — only if it genuinely helps.

## Background for beginners

For fixed destinations a plain link is enough: `<a href="/">Home</a>`. Use
`th:href="@{...}"` only when the URL is built from data.

**Optional fragments** (only if a block is copy-pasted across pages):

```html
<nav th:fragment="mainNav"> ... </nav>
```

```html
<div th:replace="~{fragments/nav :: mainNav}"></div>
```

> **Do not over-build.** No layout library, CSS, styled nav bar, or a fragment
> for every element. A handful of plain links is enough.

## Worked example

```html
<nav>
    <a href="/">Home</a> |
    <a href="/accounts">Accounts</a> |
    <a href="/businesses">Businesses</a> |
    <a href="/payments/new">New payment</a>
</nav>
```

## Your task

Add plain links so you can reach the accounts list, businesses list, payment
form, and get back from a details page. Extract a nav fragment only if
duplication genuinely bothers you.

## Verify

From the home page, do every main journey using only clicks — never typing a URL.

## Done when

Every main page is reachable by clicking, no CSS/layout framework was added, and
any fragment clearly reduced duplication.

---

# EHB Ticket TH-13 — Confirm the REST API Still Works

## Objective

Prove the Thymeleaf UI didn't break the Stage 2 REST API. Two front doors, one
set of services.

## Background for beginners

```text
REST client → @RestController ┐
                              ├→ Services → Repository → Domain
Browser     → @Controller ────┘
```

Both controllers are adapters: translate request → service call → response (JSON
or HTML). Rules live in services/domain, which is why both can call the same
service and neither should call the other.

## Your task

1. `mvn test` — all Stage 1 and 2 tests still green.
2. Spot-check REST (should still be JSON/plain text):

   ```text
   GET  /api/status
   GET  /api/accounts
   GET  /api/accounts/1111
   GET  /api/accounts/1111/transactions
   POST /api/accounts/1111/deposit     { "depositAmount": 25.00 }
   POST /api/accounts/1111/withdraw    { "withdrawAmount": 10.00 }
   POST /api/payments                  { "sourceAccountNumber": "1111", "targetAccountNumber": "4444", "paymentAmount": 5.00 }
   GET  /api/businesses
   ```

3. Confirm UI routes return HTML.
4. Confirm no `@RestController` depends on a UI controller/Thymeleaf, and the UI
   controller calls services directly (never HTTP to `/api/...`).

## Reflection

- Why can both controller types call the same service?
- Why shouldn't the UI controller call the REST controller?
- Why is it wrong for the server to HTTP-call its own `/api/...` to reuse it?
- Which layer holds the banking rules?

## Done when

`mvn test` green; REST returns expected JSON/text; UI returns HTML; both
controllers depend only on the services.

---

# EHB Ticket TH-14 — Stage 2.5 End-to-End Checkpoint

## Objective

Walk the whole UI as a user, confirm it works together, and be able to explain
the mechanics before Stage 3. Don't add features unless the walkthrough finds a
real bug.

## Preparation

Restart to restore seed state (1111=500, 2222=0, 3333=0, 4444=500, 5555=0). Run
`mvn test` — all green.

## Browser walkthrough (clicks/forms only)

1. Home page. 2. See accounts. 3. See businesses. 4. Open a business + its
accounts. 5. Open an account, note balance. 6. View its history (empty ones say
so). 7. Deposit → redirected page shows new balance + `INCOMING` row. 8. Valid
withdrawal → updated state. 9. Valid payment `1111 → 4444`; check both accounts
changed with new transactions. 10. Trigger a failure (over-balance withdrawal or
currency mismatch) → readable message, state still valid.

## Architecture checkpoint — the deposit flow

```text
Browser GETs account page
   → Spring MVC routes GET to @Controller
   → Controller asks AccountService for account + transactions
   → Controller puts them in the Model
   → Thymeleaf renders HTML
   → Browser displays page

User submits deposit form
   → Browser sends HTTP POST form fields
   → Spring MVC routes POST to @Controller
   → Controller reads accountNumber (@PathVariable) + amount (@RequestParam)
   → Controller calls AccountService.deposit(...)
   → Service loads the Account from the repository
   → Account domain method applies the rules
   → Controller returns redirect:/accounts/{number}
   → Browser performs a fresh GET
   → Thymeleaf renders the updated state
```

Contrast the outputs:

```text
REST:         Controller → DTO → Jackson → JSON
Thymeleaf UI: Controller → Model → Thymeleaf → HTML
```

## Completion criteria

Stage 2.5 is complete when:

- every main banking operation works in the browser;
- no JavaScript is needed for normal use;
- no CSS or frontend framework was introduced;
- the REST API still works;
- both REST and UI controllers reuse the service layer;
- banking rules live in domain/service, not HTML/controllers;
- no database yet;
- `mvn test` is green;
- you can explain `@Controller`, `Model`, templates, `th:text`, `th:each`,
  `th:href`, HTML forms, `@RequestParam`, and POST/Redirect/GET;
- the UI is crude but usable.

Suggested commit message:

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

Stage 3 will replace the in-memory storage with JPA/PostgreSQL. A particularly
useful observation: because the UI talks to the **services** (not storage), neither
the REST API nor the Thymeleaf UI should need to know whether `AccountRepository`
stores data in a Java `Map` or in PostgreSQL.

The intended progression remains:

```text
Stage 1 — Plain Java domain model            COMPLETE
            ↓
Stage 2 — Spring Boot + REST                 COMPLETE
            ↓
Stage 2.5 — Minimal Spring MVC + Thymeleaf UI
            ↓
Stage 3 — JPA + PostgreSQL
            ↓
Stage 4 — Microservices + Kafka
            ↓
Stage 5 — Docker + AWS
```

Do not expand Stage 2.5 into a frontend project after these requirements are
satisfied. Its purpose is to provide a small usable UI while reinforcing Spring
MVC concepts before persistence work begins.
