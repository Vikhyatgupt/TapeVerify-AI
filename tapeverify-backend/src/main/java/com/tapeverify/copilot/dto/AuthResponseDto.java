package com.tapeverify.copilot.dto; import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor public class AuthResponseDto { private String token; private String username; private String role; }
