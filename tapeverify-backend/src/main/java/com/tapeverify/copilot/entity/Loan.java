package com.tapeverify.copilot.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "loans") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Loan {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 @Column(name="batch_id", nullable=false, length=64) private String batchId;
 @Column(name="source_file_id",length=64) private String sourceFileId;
 @Column(name="source_loan_id",length=128) private String sourceLoanId;
 @Column(name="source_row_number") private Integer sourceRowNumber;
 @Lob @Column(name="raw_values", columnDefinition="LONGTEXT") private String rawValues;
 @Column(name="borrower_name", length=255) private String borrowerName;
 @Column(name="borrower_id",length=128) private String borrowerId;
 @Column(name="principal_amount", precision=15, scale=2) private BigDecimal principalAmount;
 @Column(name="current_balance",precision=15,scale=2) private BigDecimal currentBalance;
 @Column(name="interest_rate", precision=5, scale=2) private BigDecimal interestRate;
 @Column(name="payment_status",length=32) private String paymentStatus;
 @Column(name="days_past_due") private Integer daysPastDue;
 @Column(name="borrower_state",length=8) private String borrowerState;
 @Column(name="document_status",length=64) private String documentStatus;
 @Column(name="last_updated_at") private LocalDateTime lastUpdatedAt;
 @Column(name="source_system",length=128) private String sourceSystem;
 @Column(name="loan_type",length=64) private String loanType;
 @Column(name="term_months") private Integer termMonths;
 @Column(name="loan_purpose",length=128) private String loanPurpose;
 @Column(name="credit_grade",length=32) private String creditGrade;
 @Column(name="employment_length",length=64) private String employmentLength;
 @Column(name="income_band",length=64) private String incomeBand;
 @Column(name="last_payment_date") private LocalDate lastPaymentDate;
 @Column(name="origination_date") private LocalDate originationDate;
 @Column(name="maturity_date") private LocalDate maturityDate;
 @Enumerated(EnumType.STRING) @Column(nullable=false, length=32) private LoanStatus status;
 @Column(name="sha256_hash", nullable=false, length=64) private String sha256Hash;
 @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
 @OneToMany(mappedBy="loan", cascade=CascadeType.ALL, orphanRemoval=true) @Builder.Default private List<ValidationException> validationExceptions = new ArrayList<>();
}
