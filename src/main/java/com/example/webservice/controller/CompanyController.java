package com.example.webservice.controller;

import com.example.webservice.exception.UserNonAuthenticatedException;
import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.CompaniesSortTypeDto;
import com.example.webservice.model.dto.CompanyDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.entity.Company;
import com.example.webservice.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The {@link Company} controller
 */
@RestController
@Validated
@RequestMapping("/company")
@EnableMethodSecurity
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    /**
     * Add {@link Company} to a database
     *
     * @param companyDto a {@link CompanyDto} object describes company
     * @return {@link ResponseEntity} contains the information about new {@link Company} if it didn't exist before otherwise {@link ErrorInfo}
     */

    @PostMapping("/addCompany")
    public ResponseEntity<? extends BaseDto> addCompany(@Valid @RequestBody CompanyDto companyDto) {
        return companyService.createCompany(companyDto);

    }

    /**
     * Find companies by sorting and filtering parameters
     *
     * @param companiesSortTypeDto an info for sorting and filtering companies
     * @param pageNumber           current page number
     * @param pageSize             companies count per page
     * @return {@link ResponseEntity} containing {@link List} of companies
     */
    @GetMapping("/showCompanies")
    public ResponseEntity<List<Company>> findCompanies(@Valid @RequestBody CompaniesSortTypeDto companiesSortTypeDto,
                                                       @RequestParam @Min(value = 0, message = "page number >0") int pageNumber,
                                                       @RequestParam @Min(value = 0, message = "page size > 0") int pageSize) {

        return companyService.findAllCompanies(companiesSortTypeDto, pageNumber, pageSize);

    }

    /**
     * Delete {@link Company} by company unp
     *
     * @param unp {@link Company} unp
     * @return {@link ResponseEntity} contains HttpStatus.NO_CONTENT if the company is deleted otherwise {@link ErrorInfo}
     */
    @DeleteMapping("/deleteCompany")
    public ResponseEntity<? extends BaseDto> deleteCompany(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        return companyService.deleteCompany(unp);

    }

    /**
     * Find all companies for worker by sorting and filtering parameters
     *
     * @param companiesSortTypeDto an info for sorting and filtering companies
     * @param pageNumber           current page number
     * @param pageSize             companies count per page
     * @return {@link ResponseEntity} containing a {@link List} of companies
     */
    @GetMapping("/showMyCompanies")
    @PreAuthorize("hasAuthority('worker')")
    public ResponseEntity<List<Company>> findCompaniesForDirector(@Valid @RequestBody CompaniesSortTypeDto companiesSortTypeDto,
                                                                  @RequestParam @Min(value = 0, message = "page number >0") int pageNumber,
                                                                  @RequestParam @Min(value = 0, message = "page size > 0") int pageSize) throws UserNonAuthenticatedException {

        return companyService.findAllCompaniesForDirector(companiesSortTypeDto, pageNumber, pageSize);
    }
}
