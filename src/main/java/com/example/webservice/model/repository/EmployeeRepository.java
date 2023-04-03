package com.example.webservice.model.repository;

import com.example.webservice.model.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The EmployeeRepository interface describes data access functionality for the {@link  Employee} entity.
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    /**
     * Finds {@link Employee} with the specified  passportNumber.
     *
     * @param passportNumber a {@link Employee} passportNumber
     * @return the {@link Employee} wrapped in an {@link  Optional}
     */
    Optional<Employee> findEmployeesByPassportNumber(String passportNumber);

}
