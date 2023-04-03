package com.example.webservice.service;

import com.example.webservice.exception.UserNonAuthenticatedException;
import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.CompaniesSortTypeDto;
import com.example.webservice.model.dto.CompanyDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.entity.Company;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Company} service.
 */
public interface CompanyService {
    /**
     * Create new {@link Company} and  adds information about it to the database if it didn't exist before.
     *
     * @param companyDto a {@link  CompanyDto} object describes company
     * @return {@link ResponseEntity} contains the information about new {@link Company} if it didn't exist before otherwise {@link ErrorInfo}
     */
    ResponseEntity<? extends BaseDto> createCompany(CompanyDto companyDto);

    /**
     * Finds {@link Company} with the specified unp.
     *
     * @param unp a {@link Company} unp
     * @return the {@link Company} wrapped in an {@link  Optional}
     */
    Optional<Company> findCompanyByUnp(String unp);

    /**
     * Delete  {@link Company}  information from the database if possible.
     *
     * @param unp a {@link  Company} unp
     * @return {@link ResponseEntity} contains HttpStatus.NO_CONTENT if the company is deleted otherwise {@link ErrorInfo}
     */
    ResponseEntity<? extends BaseDto> deleteCompany(String unp);

    /**
     * Finds all {@link Company} by specified parameters presented in {@link CompaniesSortTypeDto}.
     *
     * @param companiesSortTypeDto contains information for sorting and filtering all companies
     * @param pageNumber           current page number
     * @param pageSize             the companies count per page
     * @return {@link ResponseEntity} contains a {@link List} of {@link Company} objects
     */
    ResponseEntity<List<Company>> findAllCompanies(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize);

    /**
     * Finds all {@link Company} where authenticated user work as a director by specified parameters presented in {@link CompaniesSortTypeDto}.
     *
     * @param companiesSortTypeDto contains information for sorting and filtering all companies
     * @param pageNumber           current page number
     * @param pageSize             the companies count per page
     * @return {@link ResponseEntity} contains a {@link List} of {@link Company} objects
     */

    ResponseEntity<List<Company>> findAllCompaniesForDirector(CompaniesSortTypeDto companiesSortTypeDto, int pageNumber, int pageSize) throws UserNonAuthenticatedException;

    /**
     * Finds all {@link Company} for authenticated user .
     *
     * @return {@link List} of {@link Company} objects
     */
    List<Company> findCompaniesForAuthenticatedUser() throws UserNonAuthenticatedException;
}


