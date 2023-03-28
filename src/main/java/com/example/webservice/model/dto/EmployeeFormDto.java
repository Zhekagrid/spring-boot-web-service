package com.example.webservice.model.dto;

import com.example.webservice.model.entity.EmployeeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class EmployeeFormDto extends BaseDto {

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
    private EmployeeType employeeType;
    @NotNull
    private Date birthdate;
    @NotBlank
    @Pattern(regexp = "\\d{14}")
    private String passportNumber;


    public EmployeeFormDto() {
    }
}
