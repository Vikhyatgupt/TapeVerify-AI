package com.tapeverify.copilot.entity;
import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime;
@Entity @Table(name="users") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AppUser { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false,unique=true,length=128) private String username; @Column(name="password_hash",nullable=false) private String passwordHash; @Enumerated(EnumType.STRING) @Column(nullable=false,length=32) private Role role; @Column(nullable=false) private boolean active; @Column(name="created_at",nullable=false) private LocalDateTime createdAt; }
