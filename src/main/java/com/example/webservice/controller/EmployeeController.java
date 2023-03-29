package com.example.webservice.controller;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.entity.Employee;
import com.example.webservice.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/showEmployees")
    public ResponseEntity<List<Employee>> showEmployees(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        return employeeService.showEmployees(unp);
    }


    @PutMapping("/addEmployee")
    public ResponseEntity<? extends BaseDto> addEmployee(@Valid @RequestBody EmployeeFormDto employeeFormDto) {
        return employeeService.addEmployee(employeeFormDto);
    }


    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<? extends BaseDto> deleteEmployee(@RequestParam Long id) {
        return employeeService.deleteEmployee(id);
    }

    @GetMapping("/showEmployeeInMyCompanies")

    public ResponseEntity<List<Employee>> showEmployeeInMyCompany() {
        return employeeService.findEmployeesInMyCompany();
    }
}
