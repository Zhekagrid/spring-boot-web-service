package com.example.webservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "COMPANY_EMPLOYEES")
public class CompanyEmployee {
    @EmbeddedId
    private CompanyEmployeeId companyEmployeeId;

    public CompanyEmployee(CompanyEmployeeId companyEmployeeId) {
        this.companyEmployeeId = companyEmployeeId;
    }

    @ManyToOne
    @MapsId("companyId")
    private Company company;

    @ManyToOne
    @MapsId("employeeId")
    private Employee employee;


    public CompanyEmployee() {

    }
}
