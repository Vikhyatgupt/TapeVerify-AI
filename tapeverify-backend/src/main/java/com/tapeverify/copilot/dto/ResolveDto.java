package com.tapeverify.copilot.dto;
import java.math.BigDecimal; import java.time.*; import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor public class ResolveDto { private String borrowerName; private BigDecimal principalAmount; private BigDecimal currentBalance; private BigDecimal interestRate; private LocalDate originationDate; private LocalDate maturityDate; private String paymentStatus; private Integer daysPastDue; private String borrowerState; private String documentStatus; private LocalDateTime lastUpdatedAt; private String sourceSystem; private boolean applyAiSuggestion; }
