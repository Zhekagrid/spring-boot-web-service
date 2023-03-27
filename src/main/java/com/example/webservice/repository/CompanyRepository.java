package com.example.webservice.repository;


import com.example.webservice.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findCompaniesByUnp(String unp);

    List<Company> findAll(Specification<Company> specification, Pageable pageable);


}
