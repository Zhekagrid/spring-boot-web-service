package com.example.webservice.controller;

import com.example.webservice.dto.*;
import com.example.webservice.entity.Company;
import com.example.webservice.entity.Employee;
import com.example.webservice.mapper.CompanyMapper;
import com.example.webservice.mapper.EmployeeMapper;
import com.example.webservice.service.CompanyService;
import com.example.webservice.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/company")
public class CompanyController {


    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);
    private final CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;


    @PostMapping("/addCompany")
    public ResponseEntity<? extends BaseDto> processForm(@Valid @RequestBody CompanyDto companyDto) {
        Company company = companyMapper.companyDtoToCompany(companyDto);
        Optional<Company> optionalCompany = companyService.addCompany(company);
        if (optionalCompany.isPresent()) {
            CompanyDto responseCompanyDto = companyMapper.companyToCompanyDto(optionalCompany.get());
            return new ResponseEntity<>(responseCompanyDto, HttpStatus.CREATED);
        }
        ErrorInfo errorInfo = new ErrorInfo("Company already created", 400);
        return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/showCompanies")
    public ResponseEntity<List<Company>> showCompanies(@RequestBody CompaniesSortTypeDto companiesSortTypeDto,
                                                       @RequestParam @Min(value = 0, message = "page number >0") int pageNumber,
                                                       @RequestParam @Min(value = 0, message = "page size > 0") int pageSize) {

        return new ResponseEntity<>(companyService.showCompanies(companiesSortTypeDto, pageNumber, pageSize), HttpStatus.OK);


    }

    @GetMapping("/showEmployees")
    public ResponseEntity<List<Employee>> showEmployees(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        return new ResponseEntity<>(employeeService.showEmployees(unp), HttpStatus.OK);
    }


    @PutMapping("/addEmployee")
    public ResponseEntity<? extends BaseDto> addEmployee(@Valid @RequestBody EmployeeFormDto employeeFormDto) {
        String unp = employeeFormDto.getUnp();
        Optional<Company> optionalCompany = companyService.findCompanyByUnp(unp);
        if (optionalCompany.isPresent()) {
            Optional<Employee> optionalEmployee = employeeService.addEmployee(optionalCompany.get(), employeeFormDto);
            if (optionalEmployee.isPresent()) {
                EmployeeFormDto responseEmployeeDto = employeeMapper.employeeToEmployeeDto(optionalEmployee.get());
                return new ResponseEntity<>(responseEmployeeDto, HttpStatus.CREATED);
            } else {
                ErrorInfo errorInfo = new ErrorInfo("Employee already work in this company", 400);
                return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
            }
        }
        ErrorInfo errorInfo = new ErrorInfo("Company doesn't exist", 400);
        return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<? extends BaseDto> deleteEmployee(@RequestParam Long id) {
        Optional<Employee> optionalEmployee = employeeService.findEmployeeById(id);
        if (optionalEmployee.isPresent()) {
            employeeService.deleteEmployee(optionalEmployee.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        ErrorInfo errorInfo = new ErrorInfo("Employee doesn't exist", 400);
        return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteCompany")
    public ResponseEntity<? extends BaseDto> deleteCompany(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        Optional<Company> optionalCompany = companyService.findCompanyByUnp(unp);
        if (optionalCompany.isPresent()) {
            companyService.deleteCompany(unp);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        ErrorInfo errorInfo = new ErrorInfo("Company doesn't exist", 400);
        return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);

    }
}
