package com.example.webservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CompanyEmployeeId implements Serializable {
    public CompanyEmployeeId(Long companyId, Long employeeId) {
        this.companyId = companyId;
        this.employeeId = employeeId;
    }

    private Long companyId;
    private Long employeeId;

    public CompanyEmployeeId() {

    }
}
