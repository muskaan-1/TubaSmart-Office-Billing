# TubaSmart Office Leasing Billing Backend

Spring Boot backend for the TubaSmart Office Leasing Billing Solution.

## Stack

- Java 17
- Spring Boot 3.5.5
- Spring Web / Validation / Actuator
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway database migrations
- Spring Security + JWT
- JUnit 5 + Testcontainers for integration smoke tests

## B0 foundation

B0 establishes the backend foundation without changing the business-domain model yet:

- PostgreSQL credentials and application secrets are environment-driven.
- Hibernate schema generation is disabled with `ddl-auto: validate`.
- Flyway owns database schema changes.
- The current JPA model is captured by the baseline `V1__baseline_current_model.sql` migration.
- API errors use one structured `ApiError` response shape.
- Validation, malformed requests, conflicts and unexpected errors are handled centrally.
- CORS is configured from `CORS_ALLOWED_ORIGINS`.
- Actuator health/info endpoints are exposed for operational checks.
- Testcontainers provides a PostgreSQL-backed Spring context smoke test.
- GitHub Actions runs the Maven verification build.

B1 will intentionally introduce the planned domain/schema redesign as a new migration rather than modifying V1.

## Run locally

1. Start PostgreSQL. You can use the supplied Docker Compose file:

```bash
docker compose up -d postgres
```

2. Set the environment variables from `.env.example`. On PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/tubasmart"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JWT_SECRET="replace-with-a-random-secret-at-least-32-characters-long"
$env:CORS_ALLOWED_ORIGINS="http://localhost:4200"
```

3. Run:

```bash
mvn clean spring-boot:run
```

The API starts on `http://localhost:8080`.

Health check:

```text
http://localhost:8080/actuator/health
```

## First login

The hard-coded `admin / admin123` login has been removed. On first start, if no users exist, an
ADMIN user is created from `BOOTSTRAP_ADMIN_USERNAME` and `BOOTSTRAP_ADMIN_PASSWORD` (min 8 chars).
Additional users are created by an admin through `POST /api/users`.
`JWT_SECRET` is required (32+ bytes) — the app refuses to start without it.

## Migration rule

Do not edit an applied Flyway migration. Future schema changes must be additive migrations such as `V2__...sql`, `V3__...sql`, etc.

## Verification

Run the full verification suite with Docker available:

```bash
mvn clean verify
```

The integration smoke test starts PostgreSQL through Testcontainers and verifies that Spring Boot can initialize against the Flyway-managed schema while Hibernate remains in `validate` mode.


## B1–B7 coverage

- **B1:** billing-domain alignment for the HTML application: company profile, SAC/HSN metadata, charge recurrence, floor-price activity/rate basis, tenant discount/escalation/remarks, invoice references and richer invoice/receipt fields.
- **B2:** master-data starter seed and validation-oriented domain services; invoice types for Tax Invoice, Proforma, Credit Note and Debit Note.
- **B3:** monthly billing engine using active allotments, floor prices and tenant charge schedules; per-line GST from SAC/HSN; CGST/SGST vs IGST; discounts; TDS and net payable; FY numbering and duplicate-period protection.
- **B4:** receipt allocation guardrails and credit-note limits so receipts cannot over-allocate and credit notes cannot exceed the remaining invoice value.
- **B5:** invoice register, GST, SAC/HSN, customer ledger, outstanding/ageing, receipts, occupancy/rent roll, revenue and backup reports.
- **B6:** database users/roles, BCrypt-compatible password hashing, role-protected administration/audit endpoints, configurable JWT/CORS.
- **B7:** Testcontainers smoke test, billing-rule tests, CI, Dockerfile, PostgreSQL + backend Compose stack, Flyway validation and deployment documentation.

The implementation intentionally keeps the original Angular-compatible endpoints such as `/api/billing/runs/generate` while adding the richer `/api/billing/runs/generate-from-prices` flow used by the HTML business model.
