package com.example.webservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true, nullable = false)

    private Long id;
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

    public Employee(String firstName, String lastName, String patronymic, String jobTitle, Date birthdate, String passportNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.jobTitle = jobTitle;
        this.birthdate = birthdate;
        this.passportNumber = passportNumber;
    }

    public Employee() {

    }
}
