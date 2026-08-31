CREATE TABLE users (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(128) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 role VARCHAR(32) NOT NULL,
 active BOOLEAN NOT NULL DEFAULT TRUE,
 created_at DATETIME NOT NULL
);
CREATE TABLE loan_batches (
 id VARCHAR(64) PRIMARY KEY,
 source_filename VARCHAR(255) NOT NULL,
 uploaded_by VARCHAR(128) NOT NULL,
 uploaded_at DATETIME NOT NULL,
 total_rows INT NOT NULL DEFAULT 0,
 valid_rows INT NOT NULL DEFAULT 0,
 exception_rows INT NOT NULL DEFAULT 0,
 failed_rows INT NOT NULL DEFAULT 0,
 status VARCHAR(32) NOT NULL
);
ALTER TABLE loans ADD COLUMN source_row_number INT NULL AFTER batch_id;
ALTER TABLE loans ADD COLUMN raw_values LONGTEXT NULL AFTER source_row_number;
ALTER TABLE validation_exceptions ADD COLUMN severity VARCHAR(16) NOT NULL DEFAULT 'ERROR' AFTER field_name;
-- ROW_NUMBER is parsed as a MySQL window-function keyword in this DDL context.
-- Keep the application property as rowNumber, but use a non-keyword database column.
ALTER TABLE validation_exceptions ADD COLUMN csv_row_number INT NULL AFTER severity;
ALTER TABLE validation_exceptions ADD COLUMN raw_value TEXT NULL AFTER csv_row_number;
ALTER TABLE validation_exceptions ADD COLUMN normalized_value TEXT NULL AFTER raw_value;
ALTER TABLE validation_exceptions ADD COLUMN ai_confidence DECIMAL(4,3) NULL AFTER ai_suggested_value;
ALTER TABLE validation_exceptions ADD COLUMN ai_provider VARCHAR(64) NULL AFTER ai_confidence;
ALTER TABLE validation_exceptions ADD COLUMN ai_model VARCHAR(128) NULL AFTER ai_provider;
ALTER TABLE validation_exceptions ADD COLUMN ai_requested_at DATETIME NULL AFTER ai_model;
ALTER TABLE validation_exceptions ADD COLUMN ai_reasoning_summary TEXT NULL AFTER ai_requested_at;
ALTER TABLE validation_exceptions ADD COLUMN ai_failure TEXT NULL AFTER ai_reasoning_summary;
CREATE INDEX idx_exception_open ON validation_exceptions(is_resolved, field_name);
