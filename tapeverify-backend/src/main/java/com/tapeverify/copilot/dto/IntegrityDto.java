package com.tapeverify.copilot.dto; import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor public class IntegrityDto {private Long loanId; private boolean verified; private String status;}
