package com.tapeverify.copilot.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ExceptionDto { private Long id; private String fieldName; private String errorMessage; private String severity; private Integer rowNumber; private String rawValue; private String normalizedValue; private String aiExplanation; private String aiSuggestedValue; private java.math.BigDecimal aiConfidence; private String aiReasoningSummary; private boolean resolved; }
