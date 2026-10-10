# Review & fixes

Static review only: Maven/javac were not available when this was done, so **run `mvn clean verify` before trusting it.**

## Fixed in this pass
| # | Problem | Fix |
|---|---------|-----|
| 1 | `application.yml`: JWT secret written as `${k7Jx9...=}` (a placeholder with the secret as its *name*) -> app cannot start; secret also leaked in source | `secret: ${JWT_SECRET}`; `JwtService` fails fast if < 32 bytes. **Rotate the leaked key.** |
| 2 | DB username/password hard-coded (`postgres`/`123`) and ignored `DB_USERNAME`/`DB_PASSWORD` | Read from env |
| 3 | Hard-coded `admin/admin123` login that worked even on production | Removed; first admin created from env vars (`BootstrapAdminRunner`) |
| 4 | `invoices.billing_period` is `varchar` but entity uses `YearMonth` -> Hibernate `validate` can fail at startup / wrong mapping | `YearMonthConverter` (autoApply) |
| 5 | `AccessDeniedException` fell into the catch-all handler -> **500 instead of 403**; unauthenticated gave 403 not 401 | Dedicated handlers + JSON entry point/denied handler |
| 6 | `orElseThrow()` / `IllegalStateException` / `IllegalArgumentException` all returned 500 | Mapped to 404 / 409 / 400; unexpected errors now logged |
| 7 | Document numbers: read-modify-write on counter with no lock -> duplicate numbers under concurrency | `PESSIMISTIC_WRITE` lookup |
| 8 | Credit-note FY format `2026-2027` vs `2026-27` elsewhere | Unified |
| 9 | `PUT /api/invoices/{id}` and `PUT /api/receipts/{id}` accepted full entities: client could change totals, tenant, amount, status | Whitelisted DTOs; POST invoice forced to DRAFT |
| 10 | Receipt `DELETE` hard-deleted money records | Cancel endpoint, blocked when allocations exist; delete -> 409 |
| 11 | Invoice cancel allowed with receipts allocated | Blocked; sets `cancelledAt` |
| 12 | `/api/reports/backup` (all customer data) open to any logged-in role | ADMIN only |
| 13 | Dashboard counted DRAFT/CANCELLED invoices and cancelled receipts | ISSUED / non-cancelled only |
| 14 | `findAll().stream().filter(...)` everywhere in billing -> loads whole tables per invoice | Targeted repository queries |
| 15 | Per-line CGST/SGST/IGST columns never populated | Populated in `generateFromPreview` |
| 16 | User API: duplicate username / bad role -> 500; no password rule; admin could deactivate self | 409 / 400 / min length 8 / blocked |
| 17 | Dockerfile needed a pre-built jar; ran as root | Multi-stage build, non-root |
| 18 | `.gitignore` ignored `.env.example` | Fixed |

## Still open (needs your decision)
* **Rent escalation** (`escalationPct`) is stored but never applied in billing.
* **Allotment `discount` (flat amount)** is ignored; only `discountPct` is used.
* **No overlap check** for allotments on the same floor/period.
* **Master controllers** (`/api/masters/*`) bind JPA entities directly and copy ids by reflection; all roles can write. Add DTOs + `@PreAuthorize` per role (PROPERTY_LEASING, BILLING_FINANCE, MANAGEMENT).
* **Ledger/outstanding** use `netPayable` (after TDS) as the debit; check this matches your accounting policy. TDS is not credited again on receipt.
* `AuditService` exists but is never called. Wire it into create/cancel/allocate actions.
* Reports/dashboard still use `findAll()`; move to SQL aggregates before data grows.
* `customers.opening_balance` is a `varchar`; should be `numeric`.
* `BillingRulesTest` re-implements the arithmetic instead of calling `BillingService`; replace with real service tests.
* No pagination on list endpoints.
