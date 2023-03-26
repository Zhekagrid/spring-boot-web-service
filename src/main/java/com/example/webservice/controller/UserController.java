package com.example.webservice.controller;

import com.example.webservice.dto.LoginInfoDto;
import com.example.webservice.entity.User;
import com.example.webservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/users")
public class UserController {
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
            String password = loginInfoDto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            User user = new User(username, encodedPassword);
            User createdUser = userRepository.save(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new User("Login must be unique"), HttpStatus.BAD_REQUEST);
        }
    }


}
