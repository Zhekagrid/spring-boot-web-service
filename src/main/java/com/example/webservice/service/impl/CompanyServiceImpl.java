package com.example.webservice.service.impl;

import com.example.webservice.exception.UserNonAuthenticatedException;
import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.CompaniesSortTypeDto;
import com.example.webservice.model.dto.CompanyDto;
import com.example.webservice.model.dto.SortType;
import com.example.webservice.model.entity.Company;
import com.example.webservice.model.entity.CompanyEmployee;
import com.example.webservice.model.entity.Employee;
import com.example.webservice.model.entity.User;
import com.example.webservice.model.repository.CompanyEmployeeRepository;
import com.example.webservice.model.repository.CompanyRepository;
import com.example.webservice.model.repository.EmployeeRepository;
import com.example.webservice.service.CompanyService;
import com.example.webservice.service.UserService;
import com.example.webservice.utill.ErrorResponseEntityFactory;
import com.example.webservice.utill.mapper.CompanyMapper;
import jakarta.persistence.criteria.Expression;
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
    private static final String COMPANY_ALREADY_CREATED = "Company already created";
    private static final String COMPANY_NOT_EXIST = "Company doesn't exist";
    private final CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<? extends BaseDto> createCompany(CompanyDto companyDto) {

        Company company = companyMapper.companyDtoToCompany(companyDto);
        Optional<Company> optionalCompany = companyRepository.findCompaniesByUnp(company.getUnp());
        if (optionalCompany.isEmpty()) {
            CompanyDto responseCompanyDto = companyMapper.companyToCompanyDto(companyRepository.save(company));
            return new ResponseEntity<>(responseCompanyDto, HttpStatus.CREATED);

        }
        return ErrorResponseEntityFactory.createErrorResponseEntity(COMPANY_ALREADY_CREATED, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponseEntityFactory.createErrorResponseEntity(COMPANY_NOT_EXIST, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<Company>> findAllCompanies(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize) {
        Specification<Company> specification = createSpecification(companiesSortTypeDto);
        Pageable pageable = createPageable(companiesSortTypeDto, pageNumber, pageSize);
        return new ResponseEntity<>(companyRepository.findAll(specification, pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Company>> findAllCompaniesForDirector(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize) throws UserNonAuthenticatedException {
        Specification<Company> specification = createSpecification(companiesSortTypeDto);
        Pageable pageable = createPageable(companiesSortTypeDto, pageNumber, pageSize);
        User user = userService.getAuthenticatedUser();
        Employee employee = user.getEmployee();
        List<CompanyEmployee> companyEmployees = companyEmployeeRepository.findCompanyEmployeeByEmployeePassportNumber(employee.getPassportNumber());
        List<String> unpList = companyEmployees.stream().map(CompanyEmployee::getCompany).map(Company::getUnp).collect(Collectors.toList());
        Specification<Company> unpCompanySpecification = (root, query, criteriaBuilder) -> {
            Expression<String> expression = root.get(UNP);
            return expression.in(unpList);
        };
        List<Company> resultCompanyList = companyRepository.findAll(specification.and(unpCompanySpecification), pageable);
        return new ResponseEntity<>(resultCompanyList, HttpStatus.OK);
    }


    @Override
    public List<Company> findCompaniesForAuthenticatedUser() throws UserNonAuthenticatedException {
        User user = userService.getAuthenticatedUser();
        Employee employee = user.getEmployee();
        List<CompanyEmployee> companyEmployees = companyEmployeeRepository.findCompanyEmployeeByEmployeePassportNumber(employee.getPassportNumber());
        return companyEmployees.stream().map(CompanyEmployee::getCompany).collect(Collectors.toList());
    }

    private Specification<Company> createSpecification(CompaniesSortTypeDto companiesSortTypeDto) {
        Specification<Company> specification;
        if (companiesSortTypeDto.getName() != null) {
            specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(NAME), companiesSortTypeDto.getName());

        } else if (companiesSortTypeDto.getDateFrom() != null) {
            specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(CREATION_DATE), companiesSortTypeDto.getDateFrom());

        } else if (companiesSortTypeDto.getDateTo() != null) {
            specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(CREATION_DATE), companiesSortTypeDto.getDateTo());
        } else {
            specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.isNotNull(root.get(UNP));
        }
        return specification;
    }

    private Pageable createPageable(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize) {
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
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
