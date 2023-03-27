package com.example.webservice.service;

import com.example.webservice.dto.CompaniesSortTypeDto;
import com.example.webservice.dto.SortType;
import com.example.webservice.entity.Company;
import com.example.webservice.entity.CompanyEmployee;
import com.example.webservice.entity.Employee;
import com.example.webservice.repository.CompanyEmployeeRepository;
import com.example.webservice.repository.CompanyRepository;
import com.example.webservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.webservice.controller.FieldName.*;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    public Optional<Company> addCompany(Company company) {
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(company.getUnp());
        if (optionalCompany.isEmpty()) {
            return Optional.of(companyRepository.save(company));
        }
        return Optional.empty();
    }

    public Optional<Company> findCompanyByUnp(String unp) {
        return companyRepository.findCompaniesByUnp(unp);
    }


    public void deleteCompany(String unp) {
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

    }

    public List<Company> showCompanies(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize) {

        Specification<Company> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get(UNP));
        if (companiesSortTypeDto.getName() != null) {
            specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(NAME), companiesSortTypeDto.getName());

        } else if (companiesSortTypeDto.getDateFrom() != null) {
            specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(CREATION_DATE), companiesSortTypeDto.getDateFrom());

        } else if (companiesSortTypeDto.getDateTo() != null) {
            specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(CREATION_DATE), companiesSortTypeDto.getDateTo());
        }

        Sort sort;
        if (companiesSortTypeDto.getSortKey().equals(NAME)) {
            sort = Sort.by(NAME);
        } else {
            sort = Sort.by(CREATION_DATE);
        }

        if (companiesSortTypeDto.getSortType().equals(SortType.ASC)) {
            sort = sort.ascending();
        } else if (companiesSortTypeDto.getSortType().equals(SortType.DESC)) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return companyRepository.findAll(specification, pageable);
    }

}
