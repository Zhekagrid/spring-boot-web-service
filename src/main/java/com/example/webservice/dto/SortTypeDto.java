package com.example.webservice.dto;


import lombok.Data;

import java.util.Date;

@Data
public class SortTypeDto {
    private String name;
    private Date dateFrom;
    private Date dateTo;
    private String sortKey;
    private String sortType;
}
