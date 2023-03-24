package com.example.webservice.controller;

import com.example.webservice.dto.EmployeeFormDto;
import com.example.webservice.dto.CompaniesSortTypeDto;
import com.example.webservice.entity.Company;
import com.example.webservice.entity.Employee;
import com.example.webservice.repository.CompanyRepository;
import com.example.webservice.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
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
    public List<Company> showCompanies(@RequestBody CompaniesSortTypeDto companiesSortTypeDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> root = cq.from(Company.class);

        Predicate wherePredicate = cb.isNotNull(root.get("unp"));
        if (companiesSortTypeDto.getName() != null) {
            wherePredicate = cb.equal(root.get("name"), companiesSortTypeDto.getName());
        } else if (companiesSortTypeDto.getDateFrom() != null) {
            wherePredicate = cb.greaterThanOrEqualTo(root.get("creationDate"), companiesSortTypeDto.getDateFrom());
        } else if (companiesSortTypeDto.getDateTo() != null) {
            wherePredicate = cb.lessThanOrEqualTo(root.get("creationDate"), companiesSortTypeDto.getDateTo());
        }

        //todo make join
        cq.where(wherePredicate);
        Path<Company> companyPath = root.get("unp");
        if (companiesSortTypeDto.getSortKey().equals("name")) {
            companyPath = root.get("name");
        } else if (companiesSortTypeDto.getSortKey().equals("creationDate")) {
            companyPath = root.get("creationDate");
        }
        if (companiesSortTypeDto.getSortType().equals("asc")) {
            cq.orderBy(cb.asc(companyPath));
        } else if (companiesSortTypeDto.getSortType().equals("desc")) {
            cq.orderBy(cb.desc(companyPath));
        }

        return entityManager.createQuery(cq).getResultList();

    }

    @GetMapping("/showEmployees")
    public ResponseEntity<List<Employee>> showEmployees(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {

        Session session = entityManager.unwrap(Session.class);
        String hql = "select employees from Company where unp = :unp";
        Query<Employee> query = session.createQuery(hql, Employee.class);
        query.setParameter("unp",unp);
        return new ResponseEntity<>(query.list(),HttpStatus.OK);
    }


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
                //todo not good create every time new employee, ask
                Employee employee = new Employee(firstName, lastName, patronymic, jobTitle, birthdate, passportNumber);
                //employee.getCompanies().add(company);
                employeeRepository.save(employee);
                company.getEmployees().add(employee);
                companyRepository.save(company);

                return new ResponseEntity<>(employee, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<Employee> deleteEmployee(@RequestParam Long id) {

        employeeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteCompany")
    public ResponseEntity<Company> deleteCompany(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(unp);
        optionalCompany.ifPresent(company -> companyRepository.delete(company));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
