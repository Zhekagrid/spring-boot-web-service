package com.example.webservice.model.repository;

import com.example.webservice.model.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findEmployeesByPassportNumber(String passportNumber);

}
