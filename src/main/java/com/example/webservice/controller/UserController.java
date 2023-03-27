package com.example.webservice.controller;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.LoginInfoDto;
import com.example.webservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(value = "/signUp")
    public ResponseEntity<? extends BaseDto> signUp(@Valid @RequestBody LoginInfoDto loginInfoDto) {
        return userService.signUp(loginInfoDto);
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginInfoDto loginDto) {
        return userService.signIn(loginDto);
    }


}
