package com.example.webservice.mapper;

import com.example.webservice.dto.CompanyDto;
import com.example.webservice.entity.Company;
import org.mapstruct.Mapper;

@Mapper
public interface CompanyMapper {
    Company companyDtoToCompany(CompanyDto companyDto);

    CompanyDto companyToCompanyDto(Company company);
}
