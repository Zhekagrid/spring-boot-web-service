package com.example.webservice.model.repository;


import com.example.webservice.model.entity.CompanyEmployee;
import com.example.webservice.model.entity.CompanyEmployeeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyEmployeeRepository extends CrudRepository<CompanyEmployee, CompanyEmployeeId> {
    List<CompanyEmployee> findCompanyEmployeeByCompanyUnp(String unp);

    boolean existsByCompanyUnpAndEmployeePassportNumber(String unp, String passportNumber);

    List<CompanyEmployee> findCompanyEmployeeByEmployeePassportNumber(String passportNumber);
}
