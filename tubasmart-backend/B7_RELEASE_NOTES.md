# TubaSmart Backend B0–B7 Release Notes

This release is based on the supplied `TubaSmart_Office_Billing.html` as the functional reference and the existing Spring Boot backend as the implementation baseline.

## HTML-to-backend coverage

| HTML area | Backend support |
|---|---|
| Company Master | `/api/company` plus expanded company profile fields |
| SAC / HSN & GST | `/api/masters/sac-hsn`, type/active metadata and invoice-line tax lookup |
| Charge / Service Type | `/api/masters/charge-types`, code + recurring flag + SAC/HSN link |
| Building / Floor | Existing master APIs retained |
| Floor Price | Effective-date pricing, rate basis, active flag and remarks |
| Tenant Allotment | Occupancy dates, billing day, discount %, escalation %, remarks, charge schedules |
| Invoice Type | SALE / PROFORMA / CREDIT / DEBIT starter types |
| Monthly Billing Run | `/api/billing/runs/preview` and `/api/billing/runs/generate-from-prices` |
| GST engine | SAC/HSN rate drives tax; same-state CGST+SGST, other-state IGST |
| TDS | Customer TDS rate -> invoice TDS and net payable |
| Invoice register | `/api/reports/invoice-register` and `/api/invoices` |
| Receipts | `/api/receipts` with on-account/unallocated balance |
| Receipt allocation | `/api/receipts/allocations` with over-allocation/customer checks |
| Credit notes | `/api/credit-notes` with remaining-invoice-value validation |
| GST Summary | `/api/reports/gst` |
| SAC/HSN Summary | `/api/reports/sac` |
| Customer Ledger | `/api/reports/ledger/{customerId}` |
| Outstanding & Ageing | `/api/reports/outstanding` |
| Receipts Register | `/api/reports/receipts` |
| Occupancy & Rent Roll | `/api/reports/occupancy` |
| Revenue Summary | `/api/reports/revenue` |
| Backup export | `/api/reports/backup` |
| Users / roles | `/api/users` (ADMIN) |
| Audit | `/api/audit` (ADMIN) |

## Important migration rule

`V1__baseline_current_model.sql` is the immutable B0 baseline. B1+ changes are additive migrations (`V2`, `V3`, ...). Do not edit an already-applied migration.

## Verification limitation

Static checks were completed in the build workspace. A live Maven/Testcontainers build was **not** run here because Maven and Docker are not installed/available in this environment. Run:

```powershell
mvn clean verify
```

with Docker running for the Testcontainers integration test.
