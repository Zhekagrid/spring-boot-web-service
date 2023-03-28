package com.example.webservice.service.impl;

import com.example.webservice.model.dto.*;
import com.example.webservice.model.entity.Company;
import com.example.webservice.model.entity.CompanyEmployee;
import com.example.webservice.model.entity.Employee;
import com.example.webservice.model.repository.CompanyEmployeeRepository;
import com.example.webservice.model.repository.CompanyRepository;
import com.example.webservice.model.repository.EmployeeRepository;
import com.example.webservice.service.CompanyService;
import com.example.webservice.utill.mapper.CompanyMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.webservice.utill.FieldName.*;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Override
    public ResponseEntity<? extends BaseDto> addCompany(CompanyDto companyDto) {

        Company company = companyMapper.companyDtoToCompany(companyDto);
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(company.getUnp());
        if (optionalCompany.isEmpty()) {
            CompanyDto responseCompanyDto = companyMapper.companyToCompanyDto(companyRepository.save(company));
            return new ResponseEntity<>(responseCompanyDto, HttpStatus.CREATED);

        }

        ErrorInfo errorInfo = new ErrorInfo("Company already created", 400);
        return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
    }

    @Override
    public Optional<Company> findCompanyByUnp(String unp) {
        return companyRepository.findCompaniesByUnp(unp);
    }

    @Override
    public ResponseEntity<? extends BaseDto> deleteCompany(String unp) {
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(unp);
        if (optionalCompany.isPresent()) {
            List<CompanyEmployee> companyEmployees = companyEmployeeRepository.findCompanyEmployeeByCompanyUnp(unp);
            List<Employee> employeeList = companyEmployees.stream().map(CompanyEmployee::getEmployee).collect(Collectors.toList());
            optionalCompany.ifPresent(company -> companyRepository.delete(company));
            for (Employee employee : employeeList) {
                List<CompanyEmployee> companyByEmployee = companyEmployeeRepository.findCompanyEmployeeByEmployeePassportNumber(employee.getPassportNumber());
                if (companyByEmployee.isEmpty()) {
                    employeeRepository.delete(employee);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        ErrorInfo errorInfo = new ErrorInfo("Company doesn't exist", 400);
        return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<Company>> showCompanies(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize) {
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
        return new ResponseEntity<>(companyRepository.findAll(specification, pageable), HttpStatus.OK);
    }

}
