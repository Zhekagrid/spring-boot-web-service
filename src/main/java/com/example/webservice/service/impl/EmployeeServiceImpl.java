package com.example.webservice.service.impl;

import com.example.webservice.exception.UserNonAuthenticatedException;
import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.entity.*;
import com.example.webservice.model.repository.CompanyEmployeeRepository;
import com.example.webservice.model.repository.EmployeeRepository;
import com.example.webservice.service.EmployeeService;
import com.example.webservice.service.UserService;
import com.example.webservice.utill.ErrorResponseEntityFactory;
import com.example.webservice.utill.mapper.EmployeeMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEE_ALREADY_WORK = "Employee already work in this company";
    private static final String EMPLOYEE_NOT_EXIST = "Employee doesn't exist";
    private static final String COMPANY_NOT_EXIST = "Company doesn't exist";
    private static final String TRY_TO_ADD_OTHER_EMPLOYEE = "You are try to add other employee";
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;
    @Autowired
    private CompanyServiceImpl companyService;
    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<? extends BaseDto> addEmployee(EmployeeFormDto employeeFormDto) throws UserNonAuthenticatedException {
        String unp = employeeFormDto.getUnp();
        Optional<Company> optionalCompany = companyService.findCompanyByUnp(unp);
        if (optionalCompany.isPresent()) {
            String passportNumber = employeeFormDto.getPassportNumber();
            boolean employeeInCompanyWork = companyEmployeeRepository.existsByCompanyUnpAndEmployeePassportNumber(unp, passportNumber);
            User user = userService.getAuthenticatedUser();
            Optional<Employee> optionalEmployee = employeeRepository.findEmployeesByPassportNumber(passportNumber);
            Employee employeeFromForm = employeeMapper.employeeDtoToEmployee(employeeFormDto);
            Employee employeeFromUser = user.getEmployee();
            if (!employeeInCompanyWork) {
                Employee employee;
                //if employee doesn't work yet
                if (optionalEmployee.isEmpty() && user.getEmployee() == null) {
                    employee = employeeFromForm;
                    employee.setUser(user);
                    employeeRepository.save(employee);
                    //if employee work, but want work in a new company
                } else if (optionalEmployee.isPresent() && employeeFromUser != null && employeeFromUser.getPassportNumber().equals(passportNumber)) {
                    employee = optionalEmployee.get();
                    employee.setEmployeeType(employeeFormDto.getEmployeeType());
                    employee.setFirstName(employeeFormDto.getFirstName());
                    employee.setLastName(employeeFormDto.getLastName());
                    employee.setPatronymic(employeeFormDto.getPatronymic());
                } else {
                    return ErrorResponseEntityFactory.createErrorResponseEntity(TRY_TO_ADD_OTHER_EMPLOYEE, HttpStatus.BAD_REQUEST);
                }

                employeeRepository.save(employee);
                Company company = optionalCompany.get();
                CompanyEmployeeId companyEmployeeId = new CompanyEmployeeId(company.getCompanyId(), employee.getEmployeeId());
                CompanyEmployee companyEmployee = new CompanyEmployee(companyEmployeeId);
                companyEmployee.setEmployee(employee);
                companyEmployee.setCompany(company);
                companyEmployeeRepository.save(companyEmployee);
                EmployeeFormDto responseEmployeeDto = employeeMapper.employeeToEmployeeDto(employee);
                return new ResponseEntity<>(responseEmployeeDto, HttpStatus.CREATED);

            } //if employee work in company but want change info
            else if (optionalEmployee.isPresent() && employeeFromUser != null
                    && employeeFromUser.getPassportNumber().equals(passportNumber)
                    && !optionalEmployee.get().equals(employeeFromForm)) {
                Employee employee = optionalEmployee.get();
                employee.setEmployeeType(employeeFormDto.getEmployeeType());
                employee.setFirstName(employeeFormDto.getFirstName());
                employee.setLastName(employeeFormDto.getLastName());
                employee.setPatronymic(employeeFormDto.getPatronymic());
                employeeRepository.save(employee);
                EmployeeFormDto responseEmployeeDto = employeeMapper.employeeToEmployeeDto(employee);
                return new ResponseEntity<>(responseEmployeeDto, HttpStatus.CREATED);

            }
            return ErrorResponseEntityFactory.createErrorResponseEntity(EMPLOYEE_ALREADY_WORK, HttpStatus.BAD_REQUEST);
        }
        return ErrorResponseEntityFactory.createErrorResponseEntity(COMPANY_NOT_EXIST, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<? extends BaseDto> deleteEmployee(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employeeRepository.delete(employee);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponseEntityFactory.createErrorResponseEntity(EMPLOYEE_NOT_EXIST, HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<Employee>> findEmployees(String unp) {
        return new ResponseEntity<>(companyEmployeeRepository.findCompanyEmployeeByCompanyUnp(unp).stream().map(CompanyEmployee::getEmployee).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> findEmployeesInAuthenticatedUserCompanies() throws UserNonAuthenticatedException {
        List<Company> companies = companyService.findCompaniesForAuthenticatedUser();
        List<Employee> employees = new ArrayList<>();
        for (Company company : companies) {
            List<CompanyEmployee> companyEmployeeForSpecificCompany = companyEmployeeRepository.findCompanyEmployeeByCompanyUnp(company.getUnp());
            employees.addAll(companyEmployeeForSpecificCompany.stream().map(CompanyEmployee::getEmployee).collect(Collectors.toList()));
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


}
