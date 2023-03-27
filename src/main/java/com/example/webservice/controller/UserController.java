package com.example.webservice.controller;

import com.example.webservice.dto.LoginInfoDto;
import com.example.webservice.mapper.UserMapper;
import com.example.webservice.entity.User;
import com.example.webservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/users")
public class UserController {
    private static final String USER_LOGIN_UNIQUE = "Login must be unique";
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping(value = "/signUp")
    public ResponseEntity<User> signUp(@Valid @RequestBody LoginInfoDto loginInfoDto) {
        String username = loginInfoDto.getUsername();
        if (!userRepository.existsByUsername(username)) {
            User user = userMapper.loginDtoToUser(loginInfoDto);
            String password = user.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            User createdUser = userRepository.save(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new User(USER_LOGIN_UNIQUE), HttpStatus.BAD_REQUEST);
        }
    }


}
