package com.example.webservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.awt.print.Book;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "EMPLOYEES")
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

//    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
//    @JoinTable(name = "company_employee",
//            joinColumns = @JoinColumn(name = "employee_id"),
//            inverseJoinColumns = @JoinColumn(name = "company_id"))
//    private Set<Company> companies=new HashSet<>();


//    @ManyToMany(mappedBy = "employees",fetch = FetchType.LAZY)
//    private Set<Company> companies=new HashSet<>();
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
