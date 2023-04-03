package com.example.webservice.service;

import com.example.webservice.exception.UserNonAuthenticatedException;
import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.dto.LoginInfoDto;
import com.example.webservice.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

/**
 * The {@link User} service.
 */
public interface UserService {
    /**
     * Sign up new {@link User} if username is unique.
     *
     * @param loginInfoDto a {@link  LoginInfoDto} object describes registration data
     * @return {@link ResponseEntity} contains HttpStatus.CREATED if user signed up otherwise {@link ErrorInfo}
     */
    ResponseEntity<? extends BaseDto> signUp(LoginInfoDto loginInfoDto);

    /**
     * Sign in {@link User} if it is possible.
     *
     * @param loginDto a {@link  LoginInfoDto} object describes login details
     * @return {@link ResponseEntity} contains HttpStatus.OK or HttpStatus.UNAUTHORIZED
     */
    ResponseEntity<String> signIn(LoginInfoDto loginDto);

    /**
     * Set {@link User} lastLoginDate in a database
     *
     * @param authentication contains information about authenticated user
     */
    void setUserLastLoginDate(Authentication authentication);

    /**
     * Get authenticated {@link User}
     *
     * @return {@link User} that is authenticated
     */
    User getAuthenticatedUser() throws UserNonAuthenticatedException;
}
