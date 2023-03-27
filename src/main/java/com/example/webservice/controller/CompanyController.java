package com.example.webservice.controller;

import com.example.webservice.dto.EmployeeFormDto;
import com.example.webservice.dto.CompaniesSortTypeDto;
import com.example.webservice.dto.SortType;
import com.example.webservice.mapper.EmployeeMapper;
import com.example.webservice.entity.Company;
import com.example.webservice.entity.CompanyEmployee;
import com.example.webservice.entity.CompanyEmployeeId;
import com.example.webservice.entity.Employee;
import com.example.webservice.repository.CompanyEmployeeRepository;
import com.example.webservice.repository.CompanyRepository;
import com.example.webservice.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import static com.example.webservice.controller.FieldName.*;

@RestController
@Validated
@RequestMapping("/company")

public class CompanyController {

    private EntityManager entityManager;
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;
    private CompanyEmployeeRepository companyEmployeeRepository;
    private EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    public CompanyController(EntityManager entityManager, CompanyRepository companyRepository, EmployeeRepository employeeRepository, CompanyEmployeeRepository companyEmployeeRepository) {
        this.entityManager = entityManager;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.companyEmployeeRepository = companyEmployeeRepository;
    }

    @PostMapping("/addCompany")
    public ResponseEntity<Company> processForm(@Valid @RequestBody Company company) {
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(company.getUnp());
        if (optionalCompany.isEmpty()) {
            companyRepository.save(company);
            return new ResponseEntity<>(company, HttpStatus.CREATED);
        }
        //todo find best code
        return new ResponseEntity<>(optionalCompany.get(), HttpStatus.CREATED);
    }

    @GetMapping("/showCompanies")
    public List<Company> showCompanies(@RequestBody CompaniesSortTypeDto companiesSortTypeDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> root = cq.from(Company.class);

        Predicate wherePredicate = cb.isNotNull(root.get(UNP));
        if (companiesSortTypeDto.getName() != null) {
            wherePredicate = cb.equal(root.get(NAME), companiesSortTypeDto.getName());
        } else if (companiesSortTypeDto.getDateFrom() != null) {
            wherePredicate = cb.greaterThanOrEqualTo(root.get(CREATION_DATE), companiesSortTypeDto.getDateFrom());
        } else if (companiesSortTypeDto.getDateTo() != null) {
            wherePredicate = cb.lessThanOrEqualTo(root.get(CREATION_DATE), companiesSortTypeDto.getDateTo());
        }


        cq.where(wherePredicate);
        Path<Company> companyPath = root.get(UNP);
        if (companiesSortTypeDto.getSortKey().equals(NAME)) {
            companyPath = root.get(NAME);
        } else if (companiesSortTypeDto.getSortKey().equals(CREATION_DATE)) {
            companyPath = root.get(CREATION_DATE);
        }
        if (companiesSortTypeDto.getSortType().equals(SortType.ASC)) {
            cq.orderBy(cb.asc(companyPath));
        } else if (companiesSortTypeDto.getSortType().equals(SortType.DESC)) {
            cq.orderBy(cb.desc(companyPath));
        }

        return entityManager.createQuery(cq).getResultList();

    }

    @GetMapping("/showEmployees")
    public ResponseEntity<List<Employee>> showEmployees(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        return new ResponseEntity<>(companyEmployeeRepository.findCompanyEmployeeByCompanyUnp(unp).stream().map(CompanyEmployee::getEmployee).collect(Collectors.toList()), HttpStatus.OK);
    }


    @PutMapping("/addEmployee")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeFormDto employeeFormDto) {
        String unp = employeeFormDto.getUnp();
        String passportNumber = employeeFormDto.getPassportNumber();
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(unp);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            Optional<Employee> optionalEmployee = employeeRepository.findEmployeesByPassportNumber(passportNumber);
            Employee employee;
            if (optionalEmployee.isPresent()) {
                employee = optionalEmployee.get();
            } else {
                employee = employeeMapper.employeeDtoToEmployee(employeeFormDto);
                employeeRepository.save(employee);

            }
            CompanyEmployeeId companyEmployeeId = new CompanyEmployeeId(company.getCompanyId(), employee.getEmployeeId());
            CompanyEmployee companyEmployee = new CompanyEmployee(companyEmployeeId);
            companyEmployee.setEmployee(employee);
            companyEmployee.setCompany(company);
            companyEmployeeRepository.save(companyEmployee);
            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<Employee> deleteEmployee(@RequestParam Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            List<CompanyEmployee> companyEmployeeByEmployee = companyEmployeeRepository.findCompanyEmployeeByEmployeePassportNumber(employee.getPassportNumber());
            companyEmployeeRepository.deleteAll(companyEmployeeByEmployee);
            employeeRepository.delete(employee);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteCompany")
    public ResponseEntity<Company> deleteCompany(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        List<CompanyEmployee> companyEmployees = companyEmployeeRepository.findCompanyEmployeeByCompanyUnp(unp);
        List<Employee> employeeList = companyEmployees.stream().map(CompanyEmployee::getEmployee).collect(Collectors.toList());
        companyEmployeeRepository.deleteAll(companyEmployees);
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(unp);
        optionalCompany.ifPresent(company -> companyRepository.delete(company));
        for (Employee employee : employeeList) {
            List<CompanyEmployee> companyByEmployee = companyEmployeeRepository.findCompanyEmployeeByEmployeePassportNumber(employee.getPassportNumber());
            if (companyByEmployee.isEmpty()) {
                employeeRepository.delete(employee);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
