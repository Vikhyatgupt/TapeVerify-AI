# Five-minute demo

1. Sign in as the Data Operator and upload loan_tape.csv. Point out the per-file checksum, batch counts, malformed rows, and exceptions.
2. Upload servicer_update.csv and document_manifest.csv as supplemental sources. Open the exception queue and show conflict exceptions.
3. Sign in as Reviewer. Search LN-1002, filter HIGH severity, open its exception, and generate an AI explanation. Show prompt/model metadata and advisory label.
4. Correct the record, explicitly record AI-assisted or manual review, add a comment, then approve it. Show the verified-record creation event.
5. Sign in as Data Consumer. Open Verified Records, download the authenticated CSV export, then open Audit Trail to show source lineage, before/after state, actor, time, and integrity check.

Close by opening ARCHITECTURE.md and AI_DEVELOPMENT_LOG.md, mentioning that supplemental-source conflicts are intentionally human-reviewed rather than auto-merged.
