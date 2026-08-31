package com.tapeverify.copilot.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AiSuggestionDto { private Long exceptionId; private String fieldName; private String explanation; private String suggestedValue; private java.math.BigDecimal confidence; private String reasoningSummary; private String provider; private String model; private java.time.LocalDateTime requestedAt; private String prompt; private boolean fallback; }
