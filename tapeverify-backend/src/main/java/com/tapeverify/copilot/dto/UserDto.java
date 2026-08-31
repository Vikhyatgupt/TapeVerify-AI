package com.tapeverify.copilot.dto; import lombok.*; import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor public class UserDto {private Long id;private String username;private String role;private boolean active;private LocalDateTime createdAt;}
