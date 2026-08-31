# TapeVerify AI

TapeVerify AI is a reviewer-led CSV loan-data quality workflow. It normalizes each uploaded row, records exceptions without discarding good rows, provides advisory AI/rule guidance, and keeps an append-only audit trail.

## Prerequisites

Java 17+, Maven, Node 18+, and MySQL 8 (or Docker Desktop). The frontend uses `VITE_API_BASE_URL`, defaulting to `http://localhost:8080/api/v1`.

Set secrets in your shell; never commit them:

```powershell
$env:DB_URL = 'jdbc:mysql://localhost:3306/tapeverify?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true'
$env:DB_USER = 'root'
$env:DB_PASS = '<mysql-password>'
$env:JWT_SECRET = '<base64-encoded-secret-that-decodes-to-at-least-32-bytes>'
$env:BOOTSTRAP_ADMIN_USERNAME = 'admin'
$env:BOOTSTRAP_ADMIN_PASSWORD = '<development-password>'
```

The admin account is created only when both bootstrap variables are supplied.

## Run locally

The simplest PowerShell option prompts for the two passwords, performs a Maven build, and starts the API. It also creates a temporary secure JWT secret if one is not supplied:

```powershell
.\scripts\start-backend.ps1
```

On Windows Command Prompt, double-click or run the launcher instead:

```bat
run-backend.cmd
```

To only build the backend, run `build-backend.cmd`.

MySQL must be running first. To use a persistent JWT secret across restarts, pass it explicitly:

```powershell
.\scripts\start-backend.ps1 -JwtSecret '<base64-secret-decoding-to-at-least-32-bytes>'
```

Alternatively, configure the environment variables above and use Maven directly. Maven does **not** support `mvn run build`:

```powershell
cd tapeverify-backend; mvn clean package
cd tapeverify-backend; mvn spring-boot:run
cd ../tapeverify-frontend; npm install; npm run dev
```

For Docker, additionally set `MYSQL_PASSWORD`, `MYSQL_ROOT_PASSWORD`, `JWT_SECRET`, and `BOOTSTRAP_ADMIN_PASSWORD`, then run `docker compose up --build`. The full application is available at `http://localhost:5173`; the API is at `http://localhost:8080`. The API health endpoint is `/actuator/health`; OpenAPI/Swagger is at `/swagger-ui/index.html`.

For a quick Docker setup, copy `.env.example` to `.env`, replace every placeholder with a local secret, and run the command above. `.env` is ignored by Git.

### Resetting an early local development database

If an earlier startup failed while applying Flyway migration V3 (the log mentions `row_number`), reset only the local `tapeverify` schema and start the application again. This removes local development data; do not use it for a shared or production database.

```sql
DROP DATABASE tapeverify;
```

The application recreates the database and applies the corrected migrations on the next start because the default local JDBC URL includes `createDatabaseIfNotExist=true`.

## AI profile

The default profile needs no API key and returns deterministic rule-based guidance. For OpenAI-backed explanations, set `OPENAI_API_KEY`, then set `SPRING_PROFILES_ACTIVE=ai`. AI is always advisory; corrections require an authenticated reviewer or administrator.

## Challenge data package and roles

Canonical CSV samples are in [samples](samples/README.md): `primary-loan-tape.csv`, `servicer-update.csv`, `document-manifest.csv`, `expected-exceptions.csv`, and `basic-validation-demo.csv`. Upload the first file as a **Primary loan tape**, then upload the two supporting files with their matching source type to demonstrate conflict detection and source lineage.

Bootstrap the administrator with the environment variables above. In **Users and roles**, create one OPERATOR, one REVIEWER, and one DATA_CONSUMER account for the judges.

## Competition API shortcuts

The authenticated API provides the challenge routes `GET /api/v1/loans`, `GET /api/v1/loans/{id}`, `GET /api/v1/exceptions` (search, severity filter, and pagination), `GET /api/v1/verified-loans`, `GET /api/v1/verified-loans/{id}`, `GET /api/v1/audit/{loanId}`, and `GET /api/v1/summary`. The original loan-scoped audit route remains available for the UI.

## Competition evidence

- [Architecture note](ARCHITECTURE.md)
- [AI Development Log](AI_DEVELOPMENT_LOG.md)
- [Five-minute demo script](DEMO_SCRIPT.md)

## 3-minute demo

1. Start the API and frontend, then sign in with the bootstrap administrator account.
2. Upload [sample-loan-tape.csv](sample-loan-tape.csv). Header aliases, currency commas, percentages, and common date styles are normalized row-by-row.
3. Open **Exceptions**, select a specific field exception, and request advisory guidance.
4. Correct the record and resolve it. The backend records the signed-in actor and whether the action used AI guidance.
5. Open **Audit trail**, select the record, review the before/after snapshots, and confirm the `Integrity verified` badge. Batch history shows every upload summary and malformed-row count.
