package com.example.webservice.model.repository;

import com.example.webservice.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserRepository interface describes data access functionality for the {@link User} entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Finds {@link User} with the specified  username.
     *
     * @param username a {@link User} username
     * @return the {@link User} wrapped in an {@link  Optional}
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Checks if a {@link User} with the specified username exists.
     *
     * @param username a {@link User}  username
     * @return true if {@link User} with the specified username  exists and false otherwise
     */

    boolean existsByUsername(String username);

}
