package com.example.webservice.service.impl;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.entity.Company;
import com.example.webservice.model.entity.CompanyEmployee;
import com.example.webservice.model.entity.CompanyEmployeeId;
import com.example.webservice.model.entity.Employee;
import com.example.webservice.model.repository.CompanyEmployeeRepository;
import com.example.webservice.model.repository.EmployeeRepository;
import com.example.webservice.service.EmployeeService;
import com.example.webservice.utill.mapper.EmployeeMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEE_ALREADY_WORK = "Employee already work in this company";
    private static final String EMPLOYEE_NOT_EXIST = "Employee doesn't exist";
    private static final String COMPANY_NOT_EXIST = "Company doesn't exist";
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;
    @Autowired
    private CompanyServiceImpl companyService;
    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Override
    public ResponseEntity<? extends BaseDto> addEmployee(EmployeeFormDto employeeFormDto) {
        String unp = employeeFormDto.getUnp();
        Optional<Company> optionalCompany = companyService.findCompanyByUnp(unp);
        if (optionalCompany.isPresent()) {
            String passportNumber = employeeFormDto.getPassportNumber();
            boolean employeeInCompanyWork = companyEmployeeRepository.existsByCompanyUnpAndEmployeePassportNumber(unp, passportNumber);
            if (!employeeInCompanyWork) {
                Optional<Employee> optionalEmployee = employeeRepository.findEmployeesByPassportNumber(passportNumber);
                Employee employee;
                if (optionalEmployee.isEmpty()) {
                    employee = employeeMapper.employeeDtoToEmployee(employeeFormDto);
                    employeeRepository.save(employee);

                } else {
                    employee = optionalEmployee.get();
                }
                Company company = optionalCompany.get();
                CompanyEmployeeId companyEmployeeId = new CompanyEmployeeId(company.getCompanyId(), employee.getEmployeeId());
                CompanyEmployee companyEmployee = new CompanyEmployee(companyEmployeeId);
                companyEmployee.setEmployee(employee);
                companyEmployee.setCompany(company);
                companyEmployeeRepository.save(companyEmployee);
                EmployeeFormDto responseEmployeeDto = employeeMapper.employeeToEmployeeDto(employee);
                return new ResponseEntity<>(responseEmployeeDto, HttpStatus.CREATED);

            }
            HttpStatus httpStatus=HttpStatus.BAD_REQUEST;
            ErrorInfo errorInfo = new ErrorInfo(EMPLOYEE_ALREADY_WORK, httpStatus.value());
            return new ResponseEntity<>(new BaseDto(errorInfo), httpStatus);
        }
        HttpStatus httpStatus=HttpStatus.BAD_REQUEST;
        ErrorInfo errorInfo = new ErrorInfo(COMPANY_NOT_EXIST, httpStatus.value());
        return new ResponseEntity<>(new BaseDto(errorInfo), httpStatus);
    }

    @Override
    public ResponseEntity<? extends BaseDto> deleteEmployee(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employeeRepository.delete(employee);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        HttpStatus httpStatus=HttpStatus.BAD_REQUEST;
        ErrorInfo errorInfo = new ErrorInfo(EMPLOYEE_NOT_EXIST, httpStatus.value());
        return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<Employee>> showEmployees(String unp) {
        return new ResponseEntity<>(companyEmployeeRepository.findCompanyEmployeeByCompanyUnp(unp).stream()
                .map(CompanyEmployee::getEmployee)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

}
