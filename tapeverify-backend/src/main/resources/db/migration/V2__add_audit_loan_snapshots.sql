ALTER TABLE audit_logs
  ADD COLUMN old_details LONGTEXT NULL AFTER new_hash,
  ADD COLUMN new_details LONGTEXT NULL AFTER old_details;

-- Preserve the existing hash evidence and add a readable representation for
-- legacy events. New events capture their own point-in-time snapshots in Java.
UPDATE audit_logs audit
JOIN loans loan ON loan.id = audit.loan_id
SET audit.new_details = JSON_OBJECT(
  'batchId', loan.batch_id,
  'borrowerName', loan.borrower_name,
  'principalAmount', loan.principal_amount,
  'interestRate', loan.interest_rate,
  'originationDate', loan.origination_date,
  'maturityDate', loan.maturity_date,
  'status', loan.status
)
WHERE audit.new_details IS NULL;
