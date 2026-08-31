# TapeVerify AI — Master Implementation Prompt

You are the lead full-stack engineer for **TapeVerify AI**, a Loan Data Verification Copilot. First inspect the existing repository, explain your implementation plan briefly, then implement all work below. Preserve working behavior and existing user data. Do not ask for confirmation for normal coding, migrations, tests, or documentation work.

## Product goal

Build a demo-ready web application that ingests messy loan-tape CSV files, normalizes and validates every row, guides human reviewers through AI-assisted remediation, and produces a traceable, verifiable audit history. This is a data-quality workflow, not a structured-finance product.

## Existing stack and repository

- Backend: Java 17+, Spring Boot 3.x, Spring Data JPA, MySQL 8, Flyway, Spring Security, Spring AI, Apache Commons CSV.
- Frontend: React 18, Vite, React Router v6, Tailwind CSS, React Query, Axios, Lucide icons.
- Backend root: `tapeverify-backend/`
- Frontend root: `tapeverify-frontend/`
- The project already has basic ingestion, loan validation, AI explanation, resolution, SHA-256 hashing, audit logs, Docker, and a Vercel SPA configuration.

## Required implementation work

### 1. Secure, role-based workflow — highest priority

Implement authentication and authorization with roles:

- `OPERATOR`: upload tapes and view records.
- `REVIEWER`: review exceptions, request AI assistance, and resolve records.
- `ADMIN`: manage users and access all audit/batch data.

Requirements:

- Use Spring Security with JWT bearer authentication (stateless API).
- Create a `users` table with password hashes, role, active status, created timestamp, and migrations.
- Add login endpoint and a bootstrap development admin account through environment variables only. Never commit a real password or token.
- Protect every API route by role. Do not use `permitAll()` except login and health endpoints.
- Remove `modifiedBy` from client-controlled resolution input. The backend must obtain the audit actor from the authenticated principal.
- Add a frontend login page, protected routes, logout, and role-aware navigation/actions.

### 2. Robust messy CSV ingestion and normalization

Do not fail an entire batch because one row has malformed input.

- Add a `loan_batches` entity/table with batch ID, source filename, uploaded by, uploaded timestamp, total rows, valid rows, exception rows, failed rows, and status.
- Accept common header aliases, such as `borrower`, `borrower_name`, `principal`, `principal_amount`, `rate`, `interest_rate`, `origination_date`, and `maturity_date`.
- Normalize whitespace, commas/currency symbols in amounts, percent signs in rates, and supported date formats.
- Parse rows independently. A bad date, malformed amount, or missing required field must create a visible exception with CSV row number and raw value; it must not abort the rest of the batch.
- Store the source row number and raw imported values needed for reviewer context.
- Return a detailed batch summary that includes successful and failed row counts.
- Add batch history and batch-detail API endpoints.

### 3. Validation and exception workflow

Retain and extend the existing validation rules:

- Required borrower, principal, rate, origination date, and maturity date.
- Principal must be positive.
- Interest rate must be greater than 0 and no higher than 30.
- Maturity date must be later than origination date.

Enhancements:

- Support multiple exceptions per loan and let the frontend select a specific exception.
- Include severity, field name, human-readable message, row number, original/raw value, and resolution state.
- Add filtering, sorting, pagination, and search for exceptions.
- Make resolution atomic: validate proposed corrected values before persisting, resolve only applicable exceptions, and return clear field errors.

### 4. AI copilot with structured, safe output

AI is optional in local development and must never prevent application startup when `OPENAI_API_KEY` is absent.

- Keep the existing default profile working without an API key, with deterministic rule-based fallback suggestions.
- Keep OpenAI integration behind the `ai` Spring profile.
- Generate an explanation for a specific exception, not merely the first exception on a loan.
- Use structured output with: `explanation`, `suggestedValue`, `confidence`, and `reasoningSummary`.
- Persist AI provider/model, request timestamp, response, and confidence with the exception or a related AI-review table.
- Never silently swallow an AI error. Return an understandable non-sensitive fallback response and record the failure for diagnostics.
- Make clear in the UI that AI is advisory and every change requires human approval.

### 5. Immutable, human-readable, verifiable audit history

Audit data must be trustworthy and understandable.

- Audit every creation, AI-assisted resolution, and manual override.
- Store point-in-time before/after loan snapshots at event creation. Do not rewrite historical audit events to change their meaning.
- Keep SHA-256 hashes internally, calculated from a canonical representation (fixed decimal scale, normalized strings, ISO dates, explicit null representation).
- Add an audit verification endpoint that recalculates the current record hash and returns a simple verified/mismatch result.
- Do not expose raw hash strings in the normal UI. Show a clear `Integrity verified` or `Integrity mismatch` badge instead.
- Audit API responses should show readable event action, actor, timestamp, source (manual or AI), and before/after loan details.
- Use append-only database/application behavior for audit events. Never provide update or delete endpoints for audit data.

### 6. Frontend demo experience

Deliver a clear reviewer workflow:

- Dashboard: total loans, batches, valid records, open exceptions, resolved records, and health trend/chart.
- Ingestion: file upload, expected columns, progress, batch summary, row-level failures, and link to the batch.
- Exceptions: search/filter/sort/paginate; click an exception; display raw input, normalized value, validation message, AI advice, confidence, editable correction form, and approval action.
- Batch history: list batches with uploader, filename, counts, timestamps, and drill-down.
- Audit: readable before/after details plus integrity badge; do not display raw hash values.
- Accessibility: semantic labels, keyboard-accessible controls, meaningful loading/empty/error states, responsive layout.

### 7. Production readiness and quality

- Keep MySQL 8 compatible with `GenerationType.IDENTITY`.
- Keep Vercel SPA rewrite configuration and dynamic `VITE_API_BASE_URL` handling.
- Restrict CORS to configured local and Vercel origins, supplied through environment variables where appropriate.
- Add Spring Boot Actuator health endpoint and Docker health check.
- Add OpenAPI/Swagger documentation for the API.
- Add request validation, global error responses, pagination limits, and safe file-size/type checks.
- Add `.gitignore` entries for build artifacts, `node_modules`, `.env*` secrets, and local database files.
- Add a GitHub Actions workflow that runs backend tests/package and frontend production build.

### 8. Tests and deliverables

Add tests for:

- CSV normalization and malformed-row handling.
- Each validation rule.
- Canonical SHA-256 hashing and audit verification.
- Authentication/authorization access control.
- Upload → exception → review → resolve → audit integration flow.

Update the root README with:

- Prerequisites and environment variables.
- Local MySQL and Docker Compose startup options.
- Default development login setup.
- AI profile setup.
- API documentation location.
- A short 3-minute demo script using the sample CSV.

## Constraints

- Use clean controller → service → repository → entity boundaries.
- Use Flyway migrations; never edit an already-applied migration.
- Preserve existing endpoints where practical, but version/add endpoints when needed.
- Do not put credentials, tokens, or passwords in source files.
- Do not expose raw hashes in the ordinary frontend UI.
- Do not replace valid working features with mock data.
- Run backend tests and frontend production build after implementation. Report exact results and any environmental blockers.

## Definition of done

The project is complete when a reviewer can sign in, upload a messy CSV, see valid rows and row-level exceptions, review AI/rule-based guidance, approve a correction, see an authenticated human-readable audit event, verify record integrity, browse batch history, and run the app locally or with Docker using documented steps.
