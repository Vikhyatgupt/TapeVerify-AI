# AI Development Log

Tools used: Codex/ChatGPT for architecture analysis, schema design, implementation, refactoring, test design, and documentation. Approximate AI-generated code contribution: 60%; all generated changes were read, compiled, and corrected by a human reviewer.

Representative prompts:

1. Compare this loan-data challenge against the current repository and list missing requirements.
2. Design immutable raw-file lineage for uploaded CSV loan tapes.
3. Extend validation safely for duplicates, stale records, document status, and state codes.
4. Create a role-aware reviewer workflow with explicit AI acceptance controls.
5. Make canonical record hashes stable across whitespace and decimal-scale differences.
6. Draft test cases for malformed CSV rows and validation-rule coverage.

Human review and corrections:

- AI initially suggested treating two-letter state codes as valid. This was rejected; the final implementation uses an explicit state/DC allow-list.
- AI initially suggested automatic application of servicer updates. This was rejected as unsafe; conflicting values now create reviewable exceptions.
- AI suggested exposing hashes in the verified-record view. This was rejected; hashes remain audit evidence and integrity is shown as a status.

AI was most useful for repetitive integration and test scaffolding. Human judgment was required for immutable lineage, role boundaries, audit semantics, and preventing AI suggestions from changing financial data silently.
