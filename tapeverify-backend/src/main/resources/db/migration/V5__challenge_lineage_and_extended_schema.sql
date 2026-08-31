CREATE TABLE uploaded_source_files (
 id VARCHAR(64) PRIMARY KEY,
 original_filename VARCHAR(255) NOT NULL,
 content_type VARCHAR(128) NULL,
 source_type VARCHAR(32) NOT NULL,
 content_sha256 VARCHAR(64) NOT NULL,
 uploaded_by VARCHAR(128) NOT NULL,
 uploaded_at DATETIME NOT NULL,
 file_content LONGBLOB NOT NULL
);
ALTER TABLE loan_batches ADD COLUMN source_file_id VARCHAR(64) NULL AFTER id;
ALTER TABLE loan_batches ADD COLUMN source_type VARCHAR(32) NOT NULL DEFAULT 'PRIMARY' AFTER source_file_id;
ALTER TABLE loans ADD COLUMN source_file_id VARCHAR(64) NULL AFTER batch_id;
ALTER TABLE loans ADD COLUMN loan_type VARCHAR(64) NULL AFTER source_system;
ALTER TABLE loans ADD COLUMN term_months INT NULL AFTER loan_type;
ALTER TABLE loans ADD COLUMN loan_purpose VARCHAR(128) NULL AFTER term_months;
ALTER TABLE loans ADD COLUMN credit_grade VARCHAR(32) NULL AFTER loan_purpose;
ALTER TABLE loans ADD COLUMN employment_length VARCHAR(64) NULL AFTER credit_grade;
ALTER TABLE loans ADD COLUMN income_band VARCHAR(64) NULL AFTER employment_length;
ALTER TABLE loans ADD COLUMN last_payment_date DATE NULL AFTER income_band;
ALTER TABLE validation_exceptions ADD COLUMN ai_prompt TEXT NULL AFTER ai_failure;
CREATE INDEX idx_loans_source_file ON loans(source_file_id);
