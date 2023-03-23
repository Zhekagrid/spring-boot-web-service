package com.example.webservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeFormDto {
    @Pattern(regexp = "\\d{9}")
    private String unp;
    @NotBlank
    @Size(max = 64)
    private String lastName;
    @NotBlank
    @Size(max = 64)
    private String firstName;
    @Size(max = 64)
    private String patronymic;
    @NotBlank
    @Size(max = 64)
    private String jobTitle;
    @NotNull
    private Date birthdate;
    @NotBlank
    @Pattern(regexp = "\\d{14}")
    private String passportNumber;
}
