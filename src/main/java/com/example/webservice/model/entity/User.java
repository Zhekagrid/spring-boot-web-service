package com.example.webservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "USERS")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 16)
    private String username;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column
    private LocalDate lastLoginDate;
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Employee employee;

}
