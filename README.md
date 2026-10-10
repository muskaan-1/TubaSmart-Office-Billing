# TubaSmart Office Billing

A web-based office billing management system designed to simplify invoice generation, tenant billing, receipt management, and financial reporting.

## Overview

TubaSmart Office Billing is a full-stack application built using Angular, Spring Boot, and PostgreSQL. It provides a centralized platform for managing office billing operations, customer records, invoices, receipts, and reports.

## Tech Stack

**Frontend**
- Angular
- TypeScript
- HTML and CSS

**Backend**
- Java
- Spring Boot
- Spring Data JPA
- REST APIs
- Maven

**Database**
- PostgreSQL
- Flyway database migrations

## Features

- **Dashboard:** Centralized view of billing operations.
- **Invoice Management:** Create and manage customer invoices.
- **Monthly Billing:** Support for monthly billing operations.
- **Receipt Management:** Record receipts and manage payment allocations.
- **Master Data Management:** Manage buildings, floors, floor prices, tenant allotments, customers, charge types, invoice types, and SAC/HSN/GST details.
- **Financial Reports:** Invoice register, credit note register, GST summary, customer ledger, outstanding and ageing, receipts register, occupancy and rent roll, and revenue summary.
- **Administration:** Company settings, user management, and backup/restore functionality.

## Project Structure

```text
TUBA_SMART/
├── tubasmart-backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
├── tubasmart-frontend/
│   ├── src/
│   ├── package.json
│   └── angular.json
│
└── README.md
```

## Prerequisites

Install the following before running the application:

- Java Development Kit (JDK)
- Maven
- Node.js and npm
- Angular CLI
- PostgreSQL
- Visual Studio Code or another suitable IDE

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/muskaan-1/TubaSmart-Office-Billing.git
cd TubaSmart-Office-Billing
```

### 2. Configure PostgreSQL

Create a database named:

```sql
CREATE DATABASE tubasmart;
```

Configure the database connection using environment variables.

**PowerShell (Windows):**

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/tubasmart"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="YOUR_POSTGRES_PASSWORD"
$env:JWT_SECRET="YOUR_SECURE_RANDOM_SECRET"
```

Replace the placeholder values with your actual local configuration. Do not commit real credentials to GitHub.

### 3. Run the Spring Boot backend

Open a terminal in the backend directory:

```bash
cd tubasmart-backend
mvn spring-boot:run
```

The backend is configured to run on:

```text
http://localhost:8080
```

Flyway is configured to manage database migrations. The application expects the database schema to match the migrations.

### 4. Run the Angular frontend

Open a second terminal:

```bash
cd tubasmart-frontend
npm install
npx ng serve
```

Open the frontend at:

```text
http://localhost:4200
```

If the frontend uses a different API URL or configuration, update it to match your local backend settings.

## Configuration

The backend uses environment variables for configuration:

| Variable | Purpose |
|---|---|
| `DB_URL` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET` | Secret used for JWT authentication |
| `JWT_EXPIRATION_MS` | JWT expiration duration |
| `SERVER_PORT` | Backend server port |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend origins |

## Database Setup

The project has Flyway enabled for database migrations. On startup, Flyway runs the available migration scripts, and Hibernate validates the database schema.

Use an appropriate database backup and restore process when transferring existing data to another machine. Creating a new database does not automatically transfer existing records.

## Development Workflow

1. Start PostgreSQL.
2. Configure the backend environment variables.
3. Start the Spring Boot backend.
4. Install frontend dependencies if needed.
5. Start the Angular development server.
6. Access the application through your browser.

## Future Enhancements

- Automated testing and continuous integration.
- Improved dashboard analytics.
- Enhanced role-based access control.
- Additional billing and financial reporting capabilities.
- Deployment to a cloud hosting platform.

## Author

Developed as a full-stack office billing management project using Java, Spring Boot, Angular, and PostgreSQL.
