package com.example.webservice.utill.mapper;

import com.example.webservice.model.dto.CompanyDto;
import com.example.webservice.model.entity.Company;
import org.mapstruct.Mapper;

@Mapper
public interface CompanyMapper {
    Company companyDtoToCompany(CompanyDto companyDto);

    CompanyDto companyToCompanyDto(Company company);
}
