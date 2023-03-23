package com.example.webservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;


@Entity
@Data
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name required")
    @Size(max = 256)
    private String name;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "\\d{9}", message = "Only 9 digits")
    private String unp;

    @Column(nullable = false)
    @NotNull(message = "Creation date required")
    private Date creationDate;
    @ManyToMany
    @JoinTable(name = "company_employee",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employees;
}
