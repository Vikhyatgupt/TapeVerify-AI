# TapeVerify AI architecture

TapeVerify AI separates ingestion, validation, review, verification, and consumption. React provides role-aware operator, reviewer, and data-consumer workflows. Spring Boot exposes a JWT-protected REST API, while MySQL persists immutable source evidence and workflow state through Flyway migrations.

## Lifecycle

CSV upload → immutable raw file → normalized loan row → validation exceptions → AI advisory / human review → verified canonical record → export and audit timeline

Each upload creates a source-file record with its SHA-256 checksum and a batch. Each normalized loan stores the source-file ID, batch ID, row number, and original values. Primary tapes create canonical records; servicer updates and document manifests compare against existing loan IDs and create high-severity conflict exceptions instead of silently overwriting data.

## Data and APIs

Key tables are uploaded_source_files, loan_batches, loans, validation_exceptions, reviewer_actions, verified_loans, and append-only audit_logs. The API supports the required loan, exception, verified-loan, audit, and summary routes. The challenge audit route is supplied alongside the existing loan-scoped audit route.

Validation is modular and reads thresholds and status vocabularies from validation_rules.json. Rules cover required values, numeric/date validity, balance relationships, payment consistency, document status, valid state code, staleness, and duplicate patterns.

## Trust controls and trade-offs

AI is advisory only. A recommendation is separately persisted with its provider, model, prompt metadata, timestamp, confidence, and failure diagnostics, then logged in the audit trail. Reviewers must explicitly correct, approve, reject, or comment; exports record the authenticated requester. Canonical hashing uses normalized strings, fixed decimal scale, explicit nulls, and ISO representations. This demo retains source files in MySQL for portable local setup; production would use encrypted object storage with immutable retention policies.
