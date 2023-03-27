package com.example.webservice.service;

import com.example.webservice.dto.EmployeeFormDto;
import com.example.webservice.entity.Company;
import com.example.webservice.entity.CompanyEmployee;
import com.example.webservice.entity.CompanyEmployeeId;
import com.example.webservice.entity.Employee;
import com.example.webservice.mapper.EmployeeMapper;
import com.example.webservice.repository.CompanyEmployeeRepository;
import com.example.webservice.repository.EmployeeRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;
    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    public Optional<Employee> addEmployee(Company company, EmployeeFormDto employeeFormDto) {
        String passportNumber = employeeFormDto.getPassportNumber();
        String unp = company.getUnp();
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

            CompanyEmployeeId companyEmployeeId = new CompanyEmployeeId(company.getCompanyId(), employee.getEmployeeId());
            CompanyEmployee companyEmployee = new CompanyEmployee(companyEmployeeId);
            companyEmployee.setEmployee(employee);
            companyEmployee.setCompany(company);
            companyEmployeeRepository.save(companyEmployee);
            return Optional.of(employee);
        }
        return Optional.empty();
    }


    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }


    public void deleteEmployee(Employee employee) {
        List<CompanyEmployee> companyEmployeeByEmployee = companyEmployeeRepository.findCompanyEmployeeByEmployeePassportNumber(employee.getPassportNumber());
        companyEmployeeRepository.deleteAll(companyEmployeeByEmployee);
        employeeRepository.delete(employee);

    }

    public List<Employee> showEmployees(String unp) {
        return companyEmployeeRepository.findCompanyEmployeeByCompanyUnp(unp).stream()
                .map(CompanyEmployee::getEmployee)
                .collect(Collectors.toList());
    }

}
