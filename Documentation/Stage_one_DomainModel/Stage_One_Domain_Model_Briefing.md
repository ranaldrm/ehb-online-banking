Let me read the codebase to give an accurate briefing.Here's a full briefing on the current state of the domain model, ready to pass to ChatGPT alongside the original briefing.

---

## EHB Online Banking — Domain Model State Briefing

### Purpose of this document

This is a handover briefing describing the current state of the `ehb-online-banking` Java project, to be read alongside the original `EHB_Online_Banking_Project_Briefing.md`. It is intended to give an AI tutor accurate context before advising on the Spring Boot / REST stage.

---

### Build toolchain

- Java 21, Maven (plain Maven, no Spring Boot parent yet)
- JUnit 5.10.2 (test scope only)
- No Spring dependencies whatsoever at this point
- Package: `com.ehb.banking`

---

### Domain classes

#### `Transaction` (record)
A Java 21 record. Immutable value object with fields: `identifier` (UUID string), `transactionType`, `transactionAmount` (BigDecimal), `timestamp` (LocalDateTime). Has a static factory method `Transaction.of(TransactionType, BigDecimal)` that generates the UUID and timestamp internally.

#### `Account`
Mutable class. Holds `accountNumber`, `currency` (enum), `balance` (BigDecimal), a `List<Transaction>`, and an optional `PaymentValidator`. Two constructors: one without a validator (for simple deposit/withdraw use), one with. Key behaviours:
- `deposit(BigDecimal)` — validates positive amount, records INCOMING transaction, updates balance
- `withdraw(BigDecimal)` — validates positive amount and sufficient funds, records OUTGOING transaction, updates balance
- `processOutgoingPayment(BigDecimal, Account targetAccount)` — requires a validator; creates a `Payment`, runs the validator chain, transitions the payment through CREATED → VALIDATED → APPROVED, records the OUTGOING transaction on the source account, credits the target via `processIncomingPayment`, then calls `payment.complete()` (COMPLETED)
- `processIncomingPayment(Payment)` — credits balance and records INCOMING transaction
- Stream-based query methods: `getOutgoingTransactions()`, `getIncomingTransactions()`, `getTotalOutgoingPayments()`, `getTotalIncomingTransactionSum()`
- Returns `List.copyOf()` from `getTransactions()` to prevent external mutation
- `equals`/`hashCode` based solely on `accountNumber`

#### `Business`
Holds a UUID-based `businessID`, `businessName`, a plaintext `password` field (noted as preparation for a future login feature), and a `Map<String, Account>` (HashMap internally, returned as `Map.copyOf()`). Key behaviours:
- `addAccount(Account)` — throws `DuplicateAccountNumberException` if the account number already exists
- `findAccount(String)` — returns `Optional<Account>` (null-safe lookup)
- `getAccount(String)` — convenience method that unwraps the Optional and throws `AccountNotFoundException` if absent
- `equals`/`hashCode` based on `businessID`

#### `Payment`
Mutable class representing an outgoing payment. Fields: `paymentID` (UUID), `paymentAmount` (BigDecimal), `paymentTime` (LocalDateTime), `sourceAccountNumber`, `targetAccountNumber`, `paymentStatus`. Constructor validates non-null/non-blank account numbers and a positive amount. State machine methods: `validate()`, `approve()`, `complete()`, `reject()` — each implemented with a switch expression that enforces legal transitions and throws `InvalidPaymentTransitionException` for illegal ones. Legal transitions:
- CREATED → VALIDATED → APPROVED → COMPLETED
- CREATED / VALIDATED / APPROVED → REJECTED
- COMPLETED and REJECTED are terminal states

---

### Enums

- `Currency`: GBP, EUR, USD
- `TransactionType`: INCOMING, OUTGOING
- `PaymentStatus`: CREATED, VALIDATED, APPROVED, COMPLETED, REJECTED

---

### Validation

A Composite pattern is used for payment validation:

- `PaymentValidator` — interface with a single method `validate(Payment, Account)`
- `CompositePaymentValidator` — holds an immutable `List<PaymentValidator>`, iterates and fails fast
- `PositiveAmountValidator` — checks payment amount > 0
- `CurrencyMatchValidator` — checks account currency matches an expected currency (passed at construction time)
- `SufficientFundsValidator` — checks account balance >= payment amount

Validators are stateless, reusable, and composed at the point where an `Account` is constructed.

---

### Exception hierarchy

All exceptions extend `BankingException extends RuntimeException`. Specific subtypes:
- `AccountNotFoundException`
- `DuplicateAccountNumberException`
- `ExceedsBalanceException`
- `InvalidPaymentException`
- `InvalidPaymentTransitionException`
- `NonPositiveAmountException`

All have both a `(String message)` and a `(String message, Throwable cause)` constructor.

---

### `BankingSystem` (main class)

A simple `main` method used as a manual integration test / scratchpad. Constructs accounts with validators, demonstrates deposit/withdraw/payment flows. Not part of the production domain; it's a driver class. This will likely be discarded when Spring Boot is introduced.

---

### Tests

Four test classes, all JUnit 5:

- **`SmokeTest`** — single sanity check that JUnit is wired up
- **`AccountTest`** — covers balance behaviour, deposit/withdrawal happy paths and error cases, invariant checks (failed operations don't mutate state), transaction recording, and the stream query methods
- **`BusinessTest`** — covers account management (add, lookup, duplicate prevention), `Optional` return from `findAccount`, and that `getAllAccounts()` returns an unmodifiable map
- **`PaymentTest`** — covers all valid and invalid state transitions, payment creation guards, validation failure via `processOutgoingPayment`, successful completion with balance/transaction assertions, and the invariant that a failed payment leaves account state unchanged

Test coverage is solid for the implemented behaviour. Tests use `@BeforeEach` for setup, `assertThrows` for exception cases, and `compareTo` for BigDecimal equality where scale differences could cause false negatives.

---

### What is notably absent (relevant for Spring Boot planning)

1. **No service layer** — business logic lives directly in `Account` and `Business`. Spring will introduce a service layer sitting between controllers and domain objects.
2. **No repository layer** — all state is in memory (instance variables). JPA/PostgreSQL replaces this in stage 3.
3. **No DTOs** — domain objects would be serialised directly if exposed now. The Spring stage should introduce request/response DTOs to decouple the API surface from the domain model.
4. **`CurrencyMatchValidator` has a design tension** — the expected currency is injected at validator construction time rather than read from the account. When Spring manages validators as beans, this will need thought (does it become a scoped bean? is the check moved into the account itself?).
5. **`Business.password` is plaintext** — intentionally deferred; the developer noted it is for a future login feature. Should not be serialised into API responses.
6. **`BankingSystem.main`** — currently the only integration point. Spring Boot's application context replaces this entirely.

---