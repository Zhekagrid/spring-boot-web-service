package com.example.webservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginInfoDto {
    @NotBlank(message = "Login required")
    @Size(max = 16, message = "Maximum 16 characters")
    private String username;
    @NotBlank(message = "Password required")
    @Size(max = 16,message = "Maximum 16 characters")
    private String password;
}
