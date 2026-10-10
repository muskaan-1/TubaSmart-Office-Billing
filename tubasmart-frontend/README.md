# TubaSmart Office Leasing Billing — Angular Frontend

## Requirements
- Node.js 20.19+ or 22.12+ (check with `node -v`)
- The TubaSmart backend running on `http://localhost:8080`
  (to point at a different URL, edit `src/app/core/config.ts`)

## Run
```bash
npm install
npm start
```
Open http://localhost:4200 and sign in with the admin user you created in the backend
(`BOOTSTRAP_ADMIN_USERNAME` / `BOOTSTRAP_ADMIN_PASSWORD`). There is no default login.

The backend must allow this origin: `CORS_ALLOWED_ORIGINS=http://localhost:4200`.

## Recommended data entry order
Company (set **State Code**) → SAC/HSN & GST → Charge Types → Invoice Types → Buildings → Floors →
Floor Prices → Customers → Tenant Allotments → Monthly Billing Run → Receipts (then Allocate) → Reports.

## Production build
```bash
npm run build      # output in dist/tubasmart-frontend
```

## Not built yet
Credit-note screen, user/role administration screen, audit log viewer, restore from backup,
role-based hiding of menu items (the backend already enforces roles).
