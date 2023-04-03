package com.example.webservice.service;

import com.example.webservice.exception.UserNonAuthenticatedException;
import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.entity.Company;
import com.example.webservice.model.entity.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * The {@link Employee} service.
 */
public interface EmployeeService {
    /**
     * Add new {@link Employee} to company if it is possible.
     *
     * @param employeeFormDto a {@link  EmployeeFormDto} object describes employee
     * @return {@link ResponseEntity} contains the information about new {@link Employee} if it is possible to add it otherwise {@link ErrorInfo}
     */
    ResponseEntity<? extends BaseDto> addEmployee(EmployeeFormDto employeeFormDto) throws UserNonAuthenticatedException;

    /**
     * Delete  {@link Employee}  information from the database if possible.
     *
     * @param id an {@link  Employee} id
     * @return {@link ResponseEntity} contains HttpStatus.NO_CONTENT if the employee is deleted otherwise {@link ErrorInfo}
     */
    ResponseEntity<? extends BaseDto> deleteEmployee(Long id);

    /**
     * Finds all {@link Employee} with specified {@link Company} unp .
     *
     * @param unp the company unp
     * @return {@link ResponseEntity} contains {@link List} of {@link Employee} objects
     */
    ResponseEntity<List<Employee>> findEmployees(String unp);

    /**
     * Finds all {@link Employee} who work in companies with an authorized user .
     *
     * @return {@link ResponseEntity} contains {@link List} of {@link Employee} objects
     */
    ResponseEntity<List<Employee>> findEmployeesInAuthenticatedUserCompanies() throws UserNonAuthenticatedException;
}
