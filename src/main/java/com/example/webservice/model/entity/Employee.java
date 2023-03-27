package com.example.webservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "EMPLOYEES")
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



}
