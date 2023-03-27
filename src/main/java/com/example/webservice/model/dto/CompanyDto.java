package com.example.webservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CompanyDto extends BaseDto {
    @NotBlank(message = "Name required")
    @Size(max = 256)
    private String name;

    @Pattern(regexp = "\\d{9}", message = "Only 9 digits")
    private String unp;
    @NotNull(message = "Creation date required")
    private Date creationDate;

    public CompanyDto() {
        super();
    }


}
