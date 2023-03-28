package com.example.webservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @MapsId("companyId")
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JsonIgnore
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
