package com.example.webservice.controller;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.CompaniesSortTypeDto;
import com.example.webservice.model.dto.CompanyDto;
import com.example.webservice.model.entity.Company;
import com.example.webservice.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/company")
public class CompanyController {


    @Autowired
    private CompanyService companyService;


    @PostMapping("/addCompany")
    public ResponseEntity<? extends BaseDto> processForm(@Valid @RequestBody CompanyDto companyDto) {
        return companyService.addCompany(companyDto);

    }

    @GetMapping("/showCompanies")
    public ResponseEntity<List<Company>> showCompanies(@Valid @RequestBody CompaniesSortTypeDto companiesSortTypeDto,
                                                       @RequestParam @Min(value = 0, message = "page number >0") int pageNumber,
                                                       @RequestParam @Min(value = 0, message = "page size > 0") int pageSize) {

        return companyService.findAllCompanies(companiesSortTypeDto, pageNumber, pageSize);

    }


    @DeleteMapping("/deleteCompany")
    public ResponseEntity<? extends BaseDto> deleteCompany(@RequestParam @Pattern(regexp = "\\d{9}") String unp) {
        return companyService.deleteCompany(unp);

    }

    @GetMapping("/showMyCompanies")
    public ResponseEntity<List<Company>> showMyCompanies(@Valid @RequestBody CompaniesSortTypeDto companiesSortTypeDto,
                                                         @RequestParam @Min(value = 0, message = "page number >0") int pageNumber,
                                                         @RequestParam @Min(value = 0, message = "page size > 0") int pageSize) {

        return companyService.findAllCompaniesForDirector(companiesSortTypeDto, pageNumber, pageSize);
    }
}
