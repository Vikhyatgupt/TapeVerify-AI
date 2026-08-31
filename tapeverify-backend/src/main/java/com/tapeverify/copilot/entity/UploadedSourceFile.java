package com.tapeverify.copilot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/** Immutable evidence for every file supplied to the verification workflow. */
@Entity @Table(name = "uploaded_source_files") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UploadedSourceFile {
  @Id @Column(length = 64) private String id;
  @Column(name = "original_filename", nullable = false, length = 255) private String originalFilename;
  @Column(name = "content_type", length = 128) private String contentType;
  @Column(name = "source_type", nullable = false, length = 32) private String sourceType;
  @Column(name = "content_sha256", nullable = false, length = 64) private String contentSha256;
  @Column(name = "uploaded_by", nullable = false, length = 128) private String uploadedBy;
  @Column(name = "uploaded_at", nullable = false) private LocalDateTime uploadedAt;
  @Lob @Basic(fetch = FetchType.LAZY) @Column(name = "file_content", nullable = false, columnDefinition = "LONGBLOB") private byte[] fileContent;
}
