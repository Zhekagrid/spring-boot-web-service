package com.example.webservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginInfoDto extends BaseDto{
    @NotBlank(message = "Login required")
    @Size(max = 16, message = "Maximum 16 characters")
    private String username;
    @NotBlank(message = "Password required")
    @Size(max = 16,message = "Maximum 16 characters")
    private String password;
}
