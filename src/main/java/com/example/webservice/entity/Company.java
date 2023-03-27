package com.example.webservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;


@Entity
@Data
@Table(name = "COMPANIES")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", unique = true, nullable = false)
    private Long companyId;

    @Column(nullable = false)
    @NotBlank(message = "Name required")
    @Size(max = 256)
    private String name;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "\\d{9}", message = "Only 9 digits")
    private String unp;

    @Column(name = "creation_date",nullable = false)
    @NotNull(message = "Creation date required")
    private Date creationDate;


}
