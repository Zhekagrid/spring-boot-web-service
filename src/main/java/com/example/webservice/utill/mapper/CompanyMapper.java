package com.example.webservice.utill.mapper;

import com.example.webservice.model.dto.CompanyDto;
import com.example.webservice.model.entity.Company;
import org.mapstruct.Mapper;

/**
 * {@link Company} mapper
 */
@Mapper
public interface CompanyMapper {

    /**
     * Maps {@link CompanyDto} to {@link Company}
     *
     * @param companyDto the {@link CompanyDto} object for mapping
     * @return {@link Company} mapped from companyDto
     */
    Company companyDtoToCompany(CompanyDto companyDto);

    /**
     * Maps {@link Company} to {@link CompanyDto}
     *
     * @param company the {@link Company} object for mapping
     * @return {@link CompanyDto} mapped from company
     */
    CompanyDto companyToCompanyDto(Company company);
}
