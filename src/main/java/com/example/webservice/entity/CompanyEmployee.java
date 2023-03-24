package com.example.webservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//@Data
//@Entity
//@Table(name = "COMPANY_EMPLOYEES")
public class CompanyEmployee {
//    @Id
//    @GeneratedValue
    private Long id;
    private Long companyId;
    private Long employeeId;


}
