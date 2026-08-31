package com.tapeverify.copilot.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="audit_logs") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuditLog {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="loan_id", nullable=false) private Loan loan;
 @Enumerated(EnumType.STRING) @Column(nullable=false, length=64) private AuditAction action;
 @Column(name="modified_by", nullable=false, length=128) private String modifiedBy;
 @Column(name="old_hash", length=64) private String oldHash;
 @Column(name="new_hash", nullable=false, length=64) private String newHash;
 @Lob @Column(name="old_details", columnDefinition="LONGTEXT") private String oldDetails;
 @Lob @Column(name="new_details", columnDefinition="LONGTEXT") private String newDetails;
 @Column(nullable=false) private LocalDateTime timestamp;
}
