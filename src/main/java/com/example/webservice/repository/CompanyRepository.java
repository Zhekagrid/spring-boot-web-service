package com.example.webservice.repository;


import com.example.webservice.entity.Company;
import com.example.webservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findCompaniesByUnp(String unp);

    List<Company> findCompaniesByCreationDateAfterOrderByCreationDateAsc(LocalDate date);

    List<Company> findCompaniesByCreationDateAfterOrderByCreationDateDesc(LocalDate date);

    List<Company> findCompaniesByCreationDateAfterOrderByNameAsc(LocalDate date);

    List<Company> findCompaniesByCreationDateAfterOrderByNameDesc(LocalDate date);

    List<Company> findCompaniesByCreationDateBeforeOrderByCreationDate(LocalDate date);

    List<Company> findCompaniesByCreationDateBeforeOrderByName(LocalDate date);

    List<Company> findCompaniesByNameOrderByCreationDate(String name);

    List<Company> findCompaniesByNameOrderByName(String name);



}
