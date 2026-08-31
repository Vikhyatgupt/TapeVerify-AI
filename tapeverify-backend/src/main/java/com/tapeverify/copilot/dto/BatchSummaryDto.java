package com.tapeverify.copilot.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BatchSummaryDto { private String batchId; private String sourceType; private String filename; private String uploadedBy; private java.time.LocalDateTime uploadedAt; private String status; private int totalRecords; private int validRecords; private int exceptionRecords; private int failedRecords; }
