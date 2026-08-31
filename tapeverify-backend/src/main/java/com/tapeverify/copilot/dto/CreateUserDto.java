package com.tapeverify.copilot.dto; import com.tapeverify.copilot.entity.Role; import jakarta.validation.constraints.*; import lombok.*;
@Data public class CreateUserDto {@NotBlank @Size(max=128) private String username;@NotBlank @Size(min=12,max=128) private String password;@NotNull private Role role;}
