package com.tejaswi.enterpriseworkforcemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message="Token is required")
    private String token;

    @NotBlank(message="New password is required")
    private String newPassword;

}
