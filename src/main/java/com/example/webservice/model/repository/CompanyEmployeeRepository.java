package com.example.webservice.model.repository;

import com.example.webservice.model.entity.Company;
import com.example.webservice.model.entity.CompanyEmployee;
import com.example.webservice.model.entity.CompanyEmployeeId;
import com.example.webservice.model.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The CompanyEmployeeRepository interface describes data access functionality for the {@link CompanyEmployee} entity.
 */
@Repository
public interface CompanyEmployeeRepository extends CrudRepository<CompanyEmployee, CompanyEmployeeId> {
    /**
     * Finds all {@link CompanyEmployee} with the specified {@link Company} unp.
     *
     * @param unp a {@link Company} unp
     * @return a {@link List}  of {@link CompanyEmployee} objects
     */
    List<CompanyEmployee> findCompanyEmployeeByCompanyUnp(String unp);

    /**
     * Checks if a {@link CompanyEmployee} with the specified {@link Company} unp and {@link Employee} passportNumber  exists.
     *
     * @param unp            a {@link Company}  unp
     * @param passportNumber an {@link Employee} passportNumber
     * @return true if {@link CompanyEmployee} with the specified {@link Company} unp and {@link Employee} passportNumber  exists and false otherwise
     */
    boolean existsByCompanyUnpAndEmployeePassportNumber(String unp, String passportNumber);

    /**
     * Finds all {@link CompanyEmployee} with the specified {@link Employee} passportNumber.
     *
     * @param passportNumber an {@link Employee} passportNumber
     * @return a {@link List} of {@link CompanyEmployee} objects
     */

    List<CompanyEmployee> findCompanyEmployeeByEmployeePassportNumber(String passportNumber);
}
