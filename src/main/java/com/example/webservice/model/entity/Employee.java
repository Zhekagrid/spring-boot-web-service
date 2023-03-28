package com.example.webservice.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "EMPLOYEES")
@EqualsAndHashCode(exclude = {"birthdate", "employeeId"})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true, nullable = false)

    private Long employeeId;
    @Column(nullable = false, length = 64)
    private String firstName;
    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(length = 64)
    private String patronymic;
    @Column(nullable = false, length = 64)
    private String jobTitle;
    @Column(nullable = false)
    private Date birthdate;
    @Column(nullable = false, length = 14)
    private String passportNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", orphanRemoval = true)
    private List<CompanyEmployee> companyEmployees = new ArrayList<>();

}
