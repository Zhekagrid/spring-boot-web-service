package com.example.webservice.controller;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.dto.LoginInfoDto;
import com.example.webservice.model.entity.User;
import com.example.webservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@link User} controller
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Signing up new user
     *
     * @param loginInfoDto a {@link LoginInfoDto} object contains registration data
     * @return {@link ResponseEntity} contains HttpStatus.CREATED if user signed up otherwise {@link ErrorInfo}
     */

    @PostMapping(value = "/signUp")
    public ResponseEntity<? extends BaseDto> signUp(@Valid @RequestBody LoginInfoDto loginInfoDto) {
        return userService.signUp(loginInfoDto);
    }

    /**
     * Sign in {@link User} if it is possible.
     *
     * @param loginDto a {@link  LoginInfoDto} object describes login details
     * @return {@link ResponseEntity} contains HttpStatus.OK or HttpStatus.UNAUTHORIZED
     */
    @PostMapping("/signIn")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginInfoDto loginDto) {
        return userService.signIn(loginDto);
    }


}
