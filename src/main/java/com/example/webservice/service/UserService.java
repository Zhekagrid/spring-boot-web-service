package com.example.webservice.service;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.LoginInfoDto;
import com.example.webservice.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface UserService {
    ResponseEntity<? extends BaseDto> signUp(LoginInfoDto loginInfoDto);

    ResponseEntity<String> signIn(LoginInfoDto loginDto);

    void setUserLastLoginDate(Authentication authentication);


    User getAuthenticatedUser();
}
