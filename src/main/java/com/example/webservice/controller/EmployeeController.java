package com.example.webservice.controller;

import com.example.webservice.exception.UserNonAuthenticatedException;
import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.entity.Company;
import com.example.webservice.model.entity.Employee;
import com.example.webservice.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The {@link Employee} controller
 */
@RestController
@Validated
@RequestMapping("/employee")
@EnableMethodSecurity
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Finds all {@link Employee} with specified {@link Company} unp .
     *
     * @param unp the company unp
     * @return {@link ResponseEntity} contains {@link List} of {@link Employee} objects
     */
    @GetMapping("/showEmployees")
    public ResponseEntity<List<Employee>> findEmployees(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        return employeeService.findEmployees(unp);
    }

    /**
     * Add new {@link Employee} to company if it is possible.
     *
     * @param employeeFormDto a {@link  EmployeeFormDto} object describes employee
     * @return {@link ResponseEntity} contains the information about new {@link Employee} if it is possible to add it otherwise {@link ErrorInfo}
     */

    @PutMapping("/addEmployee")
    public ResponseEntity<? extends BaseDto> addEmployee(@Valid @RequestBody EmployeeFormDto employeeFormDto) throws UserNonAuthenticatedException {
        return employeeService.addEmployee(employeeFormDto);
    }

    /**
     * Delete  {@link Employee}  information from the database if possible.
     *
     * @param id an {@link  Employee} id
     * @return {@link ResponseEntity} contains HttpStatus.NO_CONTENT if the employee is deleted otherwise {@link ErrorInfo}
     */
    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<? extends BaseDto> deleteEmployee(@RequestParam Long id) {
        return employeeService.deleteEmployee(id);
    }

    /**
     * Finds all {@link Employee} who work in companies with an authorized user .
     *
     * @return {@link ResponseEntity} contains {@link List} of {@link Employee} objects
     */
    @GetMapping("/showEmployeeInMyCompanies")
    @PreAuthorize("hasAuthority('director')")
    public ResponseEntity<List<Employee>> findEmployeeInDirectorCompany() throws UserNonAuthenticatedException {
        return employeeService.findEmployeesInAuthenticatedUserCompanies();
    }
}
