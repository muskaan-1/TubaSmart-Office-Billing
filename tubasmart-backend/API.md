# TubaSmart Backend API

Base URL: `http://localhost:8080/api`

## Authentication

`POST /auth/login`

```json
{"username":"admin","password":"<your bootstrap admin password>"}
```

Use the returned JWT as `Authorization: Bearer <token>`.

## Masters

CRUD endpoints are available under `/masters/*` for:

- customers
- buildings
- floors
- floor-prices
- allotments
- charge-schedules
- charge-types
- invoice-types
- sac-hsn

Company data is under `/company`.

## Billing

### Preview a monthly billing run

`POST /billing/runs/preview`

```json
{
  "tenantId": 1,
  "billingPeriod": "2026-10",
  "supplyState": "Karnataka",
  "companyState": "Karnataka"
}
```

The backend derives active allotment, floor prices / tenant charge schedules and SAC/HSN GST rates.

### Generate from explicit taxable amount

`POST /billing/runs/generate`

### Generate from configured floor prices / charge schedules

`POST /billing/runs/generate-from-prices`

Duplicate invoices for the same tenant and billing period are rejected unless the previous invoice was cancelled.

## Invoices

- `GET /invoices`
- `GET /invoices/{id}`
- `GET /invoices/{id}/lines`
- `POST /invoices`
- `PUT /invoices/{id}`
- `POST /invoices/{id}/cancel`

Issued invoices are not physically deleted.

## Receipts and allocation

- `GET/POST /receipts`
- `GET /receipts/{id}`
- `GET /receipts/allocations`
- `POST /receipts/allocations`
- `DELETE /receipts/allocations/{id}`

Allocation validation prevents allocating more than either the receipt amount or invoice outstanding balance.

## Credit notes

- `GET /credit-notes`
- `GET /credit-notes/{id}`
- `POST /credit-notes`
- `POST /credit-notes/{id}/cancel`

Credit note creation validates the remaining invoice value and derives the proportional tax impact.

## Reports

- `/reports/invoice-register`
- `/reports/receipts`
- `/reports/credit-notes`
- `/reports/gst`
- `/reports/sac`
- `/reports/ledger/{customerId}`
- `/reports/outstanding`
- `/reports/occupancy`
- `/reports/revenue`
- `/reports/backup`

## Notes

This backend uses PostgreSQL with Flyway migrations and Hibernate `ddl-auto: validate`. Applied migrations are immutable; add a new `Vn__...sql` migration for schema changes. The JSON backup endpoint is an application-level export, not a substitute for PostgreSQL backup/restore.

## User administration (B6)

- `GET /users` (ADMIN)
- `POST /users` (ADMIN) with `username`, `password`, and role (`ADMIN`, `PROPERTY_LEASING`, `BILLING_FINANCE`, `MANAGEMENT`)
- `PATCH /users/{id}/active?value=true|false` (ADMIN)
- `GET /audit` (ADMIN)

If no database user named `admin` exists yet, the original development login `admin/admin123` remains available for bootstrap. Provision a database user immediately for a real deployment.
