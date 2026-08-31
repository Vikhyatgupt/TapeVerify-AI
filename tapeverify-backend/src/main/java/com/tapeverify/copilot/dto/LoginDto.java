package com.tapeverify.copilot.dto; import jakarta.validation.constraints.*; import lombok.*;
@Data public class LoginDto { @NotBlank private String username; @NotBlank private String password; }
