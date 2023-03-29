package com.example.webservice.service;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.CompaniesSortTypeDto;
import com.example.webservice.model.dto.CompanyDto;
import com.example.webservice.model.entity.Company;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    ResponseEntity<? extends BaseDto> addCompany(CompanyDto companyDto);

    Optional<Company> findCompanyByUnp(String unp);

    ResponseEntity<? extends BaseDto> deleteCompany(String unp);

    ResponseEntity<List<Company>> findAllCompanies(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize);


    ResponseEntity<List<Company>> findAllCompaniesForDirector(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize);

    List<Company> findCompaniesForAuthenticatedUser();
}


