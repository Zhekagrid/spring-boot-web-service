package com.example.webservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Setter
@Getter
@Table(name = "COMPANIES")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", unique = true, nullable = false)
    private Long companyId;

    @Column(nullable = false)

    private String name;

    @Column(nullable = false, unique = true)
    private String unp;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company",orphanRemoval = true)

    private List<CompanyEmployee> companyEmployees = new ArrayList<>();

}
