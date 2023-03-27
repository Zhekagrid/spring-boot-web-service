package com.example.webservice.service;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.entity.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    ResponseEntity<? extends BaseDto> addEmployee(EmployeeFormDto employeeFormDto);

    ResponseEntity<? extends BaseDto> deleteEmployee(Long id);

    ResponseEntity<List<Employee>> showEmployees(String unp);
}
