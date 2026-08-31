CREATE TABLE loans (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, batch_id VARCHAR(64) NOT NULL, borrower_name VARCHAR(255), principal_amount DECIMAL(15,2), interest_rate DECIMAL(5,2), origination_date DATE, maturity_date DATE, status VARCHAR(32) NOT NULL, sha256_hash VARCHAR(64) NOT NULL, created_at DATETIME NOT NULL,
 INDEX idx_loans_batch_id (batch_id), INDEX idx_loans_status (status)
);
CREATE TABLE validation_exceptions (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, loan_id BIGINT NOT NULL, field_name VARCHAR(64) NOT NULL, error_message TEXT NOT NULL, ai_explanation TEXT, ai_suggested_value VARCHAR(255), is_resolved BOOLEAN NOT NULL DEFAULT FALSE,
 CONSTRAINT fk_exception_loan FOREIGN KEY (loan_id) REFERENCES loans(id), INDEX idx_exception_loan (loan_id)
);
CREATE TABLE audit_logs (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, loan_id BIGINT NOT NULL, action VARCHAR(64) NOT NULL, modified_by VARCHAR(128) NOT NULL, old_hash VARCHAR(64), new_hash VARCHAR(64) NOT NULL, timestamp DATETIME NOT NULL,
 CONSTRAINT fk_audit_loan FOREIGN KEY (loan_id) REFERENCES loans(id), INDEX idx_audit_loan (loan_id)
);
