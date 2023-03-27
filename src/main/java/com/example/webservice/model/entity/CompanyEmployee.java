package com.example.webservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "COMPANY_EMPLOYEES")
@NoArgsConstructor
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

}
