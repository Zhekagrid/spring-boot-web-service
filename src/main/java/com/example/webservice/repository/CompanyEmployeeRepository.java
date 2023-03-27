package com.example.webservice.repository;


import com.example.webservice.entity.CompanyEmployee;
import com.example.webservice.entity.CompanyEmployeeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyEmployeeRepository extends CrudRepository<CompanyEmployee, CompanyEmployeeId> {
    List<CompanyEmployee> findCompanyEmployeeByCompanyUnp(String unp);

    List<CompanyEmployee> findCompanyEmployeeByEmployeePassportNumber(String passportNumber);
}
