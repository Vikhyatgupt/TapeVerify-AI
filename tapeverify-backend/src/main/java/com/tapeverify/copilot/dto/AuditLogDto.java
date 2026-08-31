package com.tapeverify.copilot.dto;
import java.time.LocalDateTime; import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuditLogDto { private Long id; private String action; private String modifiedBy; private AuditLoanSnapshotDto oldDetails; private AuditLoanSnapshotDto newDetails; private LocalDateTime timestamp; }
