package com.example.webservice.model.repository;


import com.example.webservice.model.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The CompanyRepository interface describes data access functionality for the {@link Company} entity.
 */
@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    /**
     * Finds {@link Company} with the specified unp.
     *
     * @param unp a {@link Company} unp
     * @return the {@link Company} wrapped in an {@link  Optional}
     */
    Optional<Company> findCompaniesByUnp(String unp);

    /**
     * Finds all {@link Company} with {@link Specification} and specified {@link Pageable}.
     *
     * @param specification an {@link Specification}  used to build complex dynamic queries filtered by multiple conditions
     * @param pageable      an {@link Pageable} contains the information about the sorting, the number of elements per page and the requested page number
     * @return a {@link List} of {@link Company} objects
     */
    List<Company> findAll(Specification<Company> specification, Pageable pageable);


}
