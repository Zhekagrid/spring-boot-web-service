package com.example.webservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CompaniesSortTypeDto {
    private String name;
    private Date dateFrom;
    private Date dateTo;
    @NotBlank
    private String sortKey;
    @NotNull
    private SortType sortType;
}
