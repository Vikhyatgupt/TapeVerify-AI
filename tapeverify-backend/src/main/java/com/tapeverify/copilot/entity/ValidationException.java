package com.tapeverify.copilot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="validation_exceptions") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ValidationException {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="loan_id", nullable=false) private Loan loan;
 @Column(name="field_name", nullable=false, length=64) private String fieldName;
 @Column(nullable=false,length=16) @Builder.Default private String severity="ERROR";
 @Column(name="csv_row_number") private Integer rowNumber;
 @Lob @Column(name="raw_value",columnDefinition="TEXT") private String rawValue;
 @Lob @Column(name="normalized_value",columnDefinition="TEXT") private String normalizedValue;
 @Column(name="error_message", nullable=false, columnDefinition="TEXT") private String errorMessage;
 @Column(name="ai_explanation", columnDefinition="TEXT") private String aiExplanation;
 @Column(name="ai_suggested_value", length=255) private String aiSuggestedValue;
 @Column(name="ai_confidence",precision=4,scale=3) private java.math.BigDecimal aiConfidence;
 @Column(name="ai_provider",length=64) private String aiProvider;
 @Column(name="ai_model",length=128) private String aiModel;
 @Column(name="ai_requested_at") private java.time.LocalDateTime aiRequestedAt;
 @Lob @Column(name="ai_reasoning_summary",columnDefinition="TEXT") private String aiReasoningSummary;
 @Lob @Column(name="ai_failure",columnDefinition="TEXT") private String aiFailure;
 @Lob @Column(name="ai_prompt",columnDefinition="TEXT") private String aiPrompt;
 @Column(name="is_resolved", nullable=false) private boolean resolved;
}
