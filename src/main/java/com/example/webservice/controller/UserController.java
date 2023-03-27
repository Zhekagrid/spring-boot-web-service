package com.example.webservice.controller;

import com.example.webservice.dto.BaseDto;
import com.example.webservice.dto.ErrorInfo;
import com.example.webservice.dto.LoginInfoDto;
import com.example.webservice.entity.User;
import com.example.webservice.mapper.UserMapper;
import com.example.webservice.service.UserService;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping(value = "/users")
public class UserController {
    private static final String USER_LOGIN_UNIQUE = "Login must be unique";

    private static final String USER_SIGNED_SUCCESS = "User signed-in successfully!.";
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @PostMapping(value = "/signUp")
    public ResponseEntity<? extends BaseDto> signUp(@Valid @RequestBody LoginInfoDto loginInfoDto) {
        Optional<User> optionalUser = userService.signUp(loginInfoDto);

        if (optionalUser.isPresent()) {
            LoginInfoDto responseLoginDto = userMapper.userToLoginDto(optionalUser.get());
            return new ResponseEntity<>(responseLoginDto, HttpStatus.CREATED);

        } else {
            ErrorInfo errorInfo = new ErrorInfo(USER_LOGIN_UNIQUE, 400);
            return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/signIn")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginInfoDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(USER_SIGNED_SUCCESS, HttpStatus.OK);
    }


}
