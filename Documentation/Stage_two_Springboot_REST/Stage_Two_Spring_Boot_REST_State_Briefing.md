## Stage 2 COMPLETE — EHB Online Banking Spring Boot & REST State Briefing

### Purpose of this document

This is a handover briefing describing the state of the `ehb-online-banking`
project at the end of Stage 2, to be read alongside the original
`EHB_Online_Banking_Project_Briefing.md`, the Stage 2 planning briefing
(`EHB_Stage_2_Spring_Boot_REST_Briefing.md`), and the Stage 1 state briefing.
It is intended to give an AI tutor accurate context before advising on the
Stage 3 (JPA / PostgreSQL) stage.

Stage 2 wrapped the existing plain-Java domain model in a Spring Boot REST
application. The domain model from Stage 1 survived largely intact; an
application/API layer was added around it so external HTTP clients can
interact with EHB. Storage remains fully in memory — no JPA, Hibernate, or
PostgreSQL.

---

### Build toolchain

- Java 21, Maven
- Spring Boot 3.2.5 (via `spring-boot-dependencies` imported in
  `dependencyManagement`; the project does **not** use the Spring Boot parent
  POM)
- Dependencies: `spring-boot-starter-web`, `spring-boot-starter-validation`,
  `spring-boot-starter-test` (test scope)
- Plugins: `spring-boot-maven-plugin`, `maven-compiler-plugin`,
  `maven-surefire-plugin`
- Package root: `com.ehb.banking`
- Embedded server: Tomcat 10.1.20 on port 8080 (default), started by
  `SpringApplication.run(...)` via auto-configuration

---

### Application entry point

#### `EhbBankingApplication`
The Spring Boot entry point. Annotated `@SpringBootApplication`; `main` calls
`SpringApplication.run(EhbBankingApplication.class, args)`. This creates the
application context, triggers component scanning from `com.ehb.banking`
downward, runs auto-configuration, and starts embedded Tomcat.

#### `BankingSystem` (legacy)
The Stage 1 `main` scratchpad class still exists alongside the Spring entry
point. It is now dead weight — a second `main` method unrelated to the Spring
application. It is a candidate for deletion but was intentionally left in place
during Stage 2. Not part of the running application.

---

### Layered architecture (new in Stage 2)

The request lifecycle established is:

```
HTTP client → embedded Tomcat → Spring MVC (DispatcherServlet)
    → Controller → Service → Repository / Domain model
    → response DTO → Jackson → JSON
```

Dependencies point inward: controllers depend on services, services depend on
repositories and the domain, the domain depends on nothing above it. All
wiring is constructor injection; there is no field or setter injection and no
`@Autowired` on fields in production code.

---

### Controllers (`com.ehb.banking.controller`)

All are `@RestController`. Each holds its collaborating service via a `final`
field set through constructor injection.

#### `StatusController`
- `GET /api/status` → returns a plain `String` health message. Because the
  return type is `String`, Spring uses `StringHttpMessageConverter`, so the
  response is `text/plain`, not JSON. Useful as a contrast to the DTO-returning
  controllers.

#### `AccountController` — base path `/api/accounts`
- `GET /{accountNumber}` → `AccountResponse`
- `GET /{accountNumber}/transactions` → `List<TransactionResponse>`
- `POST /{accountNumber}/deposit` → `TransactionResponse` (body `DepositRequest`, `@Valid`)
- `POST /{accountNumber}/withdraw` → `TransactionResponse` (body `WithdrawRequest`, `@Valid`)

#### `PaymentController` — base path `/api/payments`
- `POST` → `PaymentResponse` (body `PaymentRequest`, `@Valid`). Delegates to
  `PaymentService.processPayment(...)`; contains no payment rules itself.

#### `BusinessController` — base path `/api/businesses`
- `GET /{id}` → `BusinessResponse`
- `GET /{id}/accounts` → `List<AccountResponse>` (derived from `BusinessResponse.from(...).accounts()`)

---

### Services (`com.ehb.banking.service`)

All `@Service` beans, constructor-injected, stateless. They orchestrate use
cases; they do not implement banking rules (those remain in the domain).

#### `AccountService`
Depends on `AccountRepository`. Methods:
- `getAccountByNumber(String)` — looks up or throws `AccountNotFoundException`
  (single source of the not-found policy)
- `getTransactionHistory(String)`
- `deposit(String, BigDecimal)` — delegates to `Account.deposit`
- `withdraw(String, BigDecimal)` — delegates to `Account.withdraw`

#### `PaymentService`
Depends on `AccountService` (not the repository directly, so it reuses the
not-found behaviour). `processPayment(source, target, amount)` fetches both
accounts and calls `source.processOutgoingPayment(amount, target)`, returning
the resulting `Payment`. Pure orchestration.

#### `BusinessService`
Depends on `BusinessRepository`. `getBusinessByID(String)` looks up or throws
`BusinessNotFoundException`; `getAccountsForBusiness(String)` returns the
business's accounts.

---

### Repositories (`com.ehb.banking.repository`)

In-memory only. These are `@Component` beans that also act as the seed-data
source at startup. No JPA, no database.

#### `AccountRepository`
Holds a `Map<String, Account>` (`LinkedHashMap`). Constructor seeds five
accounts: `1111` (GBP), `2222` (EUR), `3333` (USD), `4444` (GBP), `5555` (EUR),
with `1111` and `4444` funded to 500.00 via `deposit`. All accounts share a
`CompositePaymentValidator` (positive amount, sufficient funds, currency
match). Exposes `findByAccountNumber(String) : Optional<Account>` and a
defensive-copy `getAccounts()`.

#### `BusinessRepository`
Holds a `Map<String, Business>` (`LinkedHashMap`). Depends on
`AccountRepository` (constructor injection) to attach seeded accounts to
businesses. Seeds three businesses: "Dodgy Dave's" (accounts 1111, 2222),
"University of Brigadoon" (3333, 4444), "workShy Consultants LLC" (5555).
Business IDs are UUIDs generated in the `Business` constructor, so they change
on every startup; the repository logs each ID via SLF4J at INFO level at
construction time, which is how a tester recovers the current IDs from the
startup console.

---

### DTOs (`com.ehb.banking.dto`)

All Java records. Response DTOs use a static `from(...)` factory to map from a
domain object; the domain object is never serialised directly. This is the
explicit API/domain boundary.

**Response DTOs (outbound, serialised by Jackson):**
- `AccountResponse` — `accountNumber`, `currency`, `balance` (no transaction
  list, no validator)
- `TransactionResponse` — `identifier`, `transactionType`, `transactionAmount`,
  `timestamp`
- `PaymentResponse` — `sourceAccountNumber`, `targetAccountNumber`,
  `paymentAmount`, `paymentStatus`, `paymentID`, `paymentTime`, `currency`
- `BusinessResponse` — `businessID`, `businessName`, `List<AccountResponse>`.
  Deliberately **omits** the `Business.password` field, so the plaintext
  password can never be serialised into an API response.
- `ErrorResponse` — single `message` field; used by the exception handler.

**Request DTOs (inbound, deserialised by Jackson, validated by `@Valid`):**
- `PaymentRequest` — `@NotBlank sourceAccountNumber`, `@NotBlank targetAccountNumber`,
  `@Positive @NotNull paymentAmount`
- `DepositRequest` — `@Positive @NotNull depositAmount`
- `WithdrawRequest` — `@Positive @NotNull withdrawAmount`

Request and response DTOs are intentionally different shapes — e.g. a payment
request carries only what is needed to initiate a payment, while the response
adds server-generated `paymentID`, `paymentTime`, and `paymentStatus`.

---

### Exception handling (`com.ehb.banking.exceptions`)

#### `GlobalExceptionHandler`
A `@RestControllerAdvice` that maps domain exceptions to HTTP status codes and
returns an `ErrorResponse` (`{ "message": ... }`) body:
- `AccountNotFoundException` → 404
- `BusinessNotFoundException` → 404
- `NonPositiveAmountException` → 400
- `ExceedsBalanceException` → 409
- `DuplicateAccountNumberException` → 409
- `InvalidPaymentException` → 400
- `InvalidPaymentTransitionException` → 400

New exception since Stage 1: `BusinessNotFoundException` (extends
`BankingException`). The rest of the Stage 1 hierarchy is unchanged.

**Known inconsistency (intentional, deferred):** Bean Validation failures
(`@Valid` rejecting a blank/non-positive field) and malformed JSON are **not**
handled by `GlobalExceptionHandler`. They fall through to Spring Boot's default
error response shape (`{timestamp, status, error, path}`) with a 400 status.
So domain errors return the clean `ErrorResponse` shape, while validation and
parse errors return Spring's default shape. Both are predictable and non-500,
which satisfies the Stage 2 requirements, but the two error-body shapes are not
unified. Unifying them would mean adding handlers for
`MethodArgumentNotValidException` and `HttpMessageNotReadableException`.

---

### Validation: two layers

- **API/input validation** — Jakarta Bean Validation annotations on request
  DTOs, triggered by `@Valid` in the controller. Runs after Jackson builds the
  DTO but before the controller body. Rejects structurally bad input (blank
  account number, non-positive amount) with 400.
- **Domain validation** — the Stage 1 validator chain
  (`PositiveAmountValidator`, `CurrencyMatchValidator`, `SufficientFundsValidator`
  composed in `CompositePaymentValidator`) plus the invariants inside `Account`
  and `Payment`. These still fire regardless of how an object is created, so
  the domain protects itself even when reached from non-HTTP code. Insufficient
  funds is caught here and mapped to 409.

The positivity rule is intentionally checked in both layers; the domain check
is the real safety net, the DTO check is a fast client-facing guard.

---

### Domain model and validators

Unchanged from Stage 1 in substance. The domain classes (`Account`, `Business`,
`Payment`, `Transaction`, the `Currency` / `TransactionType` / `PaymentStatus`
enums) and the Composite validator chain carry **no Spring annotations** and
are constructed with `new` (by repositories or by the payment flow), not
managed as beans. The Stage 1 `CurrencyMatchValidator` design tension (expected
currency supplied at construction) was left as-is; validators are composed in
the repository when accounts are created, not turned into beans.

---

### Tests

Stage 1 domain tests are retained and still pass, provable without starting
Spring: `AccountTest`, `BusinessTest`, `PaymentTest`, plus `SmokeTest`.

New Stage 2 controller/web-layer tests use `@WebMvcTest` with `MockMvc` and
`@MockBean` (Mockito), loading only the web slice with the service mocked:

- **`AccountControllerTest`** (`@WebMvcTest(AccountController.class)`) — existing
  account → 200 with JSON body assertions; unknown account → 404; valid deposit
  → 200 with `TransactionResponse`; zero deposit amount → 400 (validation);
  withdrawal exceeding balance → 409.
- **`PaymentControllerTest`** (`@WebMvcTest(PaymentController.class)`) — valid
  payment → 200 with `COMPLETED` status; blank source account → 400
  (validation); insufficient funds → 409. Note: it mocks both `PaymentService`
  and `AccountService`, because the context fails to load the payment web slice
  without the transitive `AccountService` bean present.

These tests prove the controller/HTTP concerns (routing, JSON binding,
validation, status mapping) in isolation from the real service and domain,
while the Stage 1 tests prove the domain rules independently of Spring. Both
layers are needed and test different things.

---

### Manual verification performed (S2-20 checkpoint)

The full end-to-end scenario was exercised through Postman and behaved as
expected:

- Happy path: read accounts, read transaction history, POST a payment
  (1111 → 4444, £100), confirmed balances moved (400.00 / 600.00) and matching
  `OUTGOING` / `INCOMING` transactions were recorded on each account; deposit
  and withdrawal on account 2222 adjusted the balance correctly.
- Failure paths, all returning predictable non-500 responses: unknown account
  → 404 (`ErrorResponse`); unknown business → 404 (`ErrorResponse`); blank
  account number → 400 (Spring default shape); negative amount → 400 (Spring
  default shape); insufficient funds → 409 (`ErrorResponse`); malformed JSON
  → 400 (Spring default shape, failing at Jackson before `@Valid`).

`mvn test` and `mvn clean package` both run green.

---

### What is notably present now (vs Stage 1)

1. **Service layer** — orchestration extracted out of the domain into
   `AccountService`, `PaymentService`, `BusinessService`.
2. **Repository layer (in-memory)** — `AccountRepository`, `BusinessRepository`
   as the seed-data source and lookup abstraction. This is the seam JPA will
   replace in Stage 3.
3. **DTO layer** — request/response records decoupling the API from the domain,
   with the `Business` password shielded from serialisation.
4. **REST API** — account, transaction, business/account, deposit, withdrawal,
   and payment endpoints, plus a status endpoint.
5. **Exception-to-HTTP mapping** — via `@RestControllerAdvice`.
6. **Input validation** — Jakarta Bean Validation on request DTOs.
7. **Web-layer tests** — `@WebMvcTest` + `MockMvc` alongside the retained
   domain tests.

---

### What is notably absent (relevant for Stage 3 planning)

1. **No persistence** — all state is in memory and reset on every restart
   (including the randomly generated business UUIDs). JPA/Hibernate,
   `@Entity`, `@Repository`, Flyway, and PostgreSQL are Stage 3.
2. **Unified error bodies not implemented** — validation/parse errors still use
   Spring's default shape rather than `ErrorResponse` (see exception handling
   note above).
3. **`BankingSystem.java` still present** — the Stage 1 driver class is now
   redundant beside `EhbBankingApplication` and should be removed.
4. **Business endpoints only lightly exercised manually** — the endpoints exist
   and are covered structurally, but were verified more by inspection than by
   repeated live calls during the S2-20 checkpoint.
5. **No security** — the `Business.password` field remains plaintext and unused;
   authentication (Spring Security, JWT, etc.) is deliberately out of scope
   until a later stage.
6. **`CurrencyMatchValidator` design tension carried forward** — expected
   currency is still supplied at construction; not resolved into a bean, by
   design.

---

### Overall stage progression

```
Stage 1 — COMPLETE
Plain Java domain model
        ↓
Stage 2 — COMPLETE
Spring Boot + REST (in-memory)
        ↓
Stage 3 — NEXT
JPA + PostgreSQL + testing
        ↓
Stage 4
Microservices + Kafka
        ↓
Stage 5
Docker + AWS + consolidation
```
