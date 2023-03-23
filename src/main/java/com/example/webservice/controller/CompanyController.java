package com.example.webservice.controller;

import com.example.webservice.dto.EmployeeFormDto;
import com.example.webservice.dto.SortTypeDto;
import com.example.webservice.entity.Company;
import com.example.webservice.entity.Employee;
import com.example.webservice.repository.CompanyRepository;
import com.example.webservice.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private EntityManager entityManager;
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;

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
    public List<Company> showCompanies(@RequestBody SortTypeDto sortTypeDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> root = cq.from(Company.class);
        Predicate wherePredicate = cb.isNotNull(root.get("unp"));
        if (sortTypeDto.getName() != null) {
            wherePredicate = cb.equal(root.get("name"), sortTypeDto.getName());
        } else if (sortTypeDto.getDateFrom() != null) {
            wherePredicate = cb.greaterThanOrEqualTo(root.get("creationDate"), sortTypeDto.getDateFrom());
        } else if (sortTypeDto.getDateTo() != null) {
            wherePredicate = cb.lessThanOrEqualTo(root.get("creationDate"), sortTypeDto.getDateTo());
        }
        cq.where(wherePredicate);
        Path<Company> companyPath = root.get("unp");
        if (sortTypeDto.getSortKey().equals("name")) {
            companyPath = root.get("name");
        } else if (sortTypeDto.getSortKey().equals("creationDate")) {
            companyPath = root.get("creationDate");
        }
        if (sortTypeDto.getSortType().equals("asc")) {
            cq.orderBy(cb.asc(companyPath));
        } else if (sortTypeDto.getSortType().equals("desc")) {
            cq.orderBy(cb.desc(companyPath));
        }
        return entityManager.createQuery(cq).getResultList();

    }

//@PutMapping("/addEmployee")
//public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeFormDto employeeFormDto){
//
//return null;
//}

    @PutMapping("/addEmployee")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeFormDto employeeFormDto) {
        String unp = employeeFormDto.getUnp();
        String passportNumber = employeeFormDto.getPassportNumber();
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(unp);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            Set<Employee> employees = company.getEmployees();
            boolean existsWithPassportNumber = employees.stream().anyMatch(employee -> employee.getPassportNumber().equals(passportNumber));
            if (!existsWithPassportNumber) {
                String firstName = employeeFormDto.getFirstName();
                String lastName = employeeFormDto.getLastName();
                String patronymic = employeeFormDto.getPatronymic();
                Date birthdate = employeeFormDto.getBirthdate();
                String jobTitle = employeeFormDto.getJobTitle();
                Employee employee = new Employee(firstName, lastName, patronymic, jobTitle, birthdate, passportNumber);
                employeeRepository.save(employee);
                employees.add(employee);
                company.setEmployees(employees);
                companyRepository.save(company);
                return new ResponseEntity<>(employee, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
