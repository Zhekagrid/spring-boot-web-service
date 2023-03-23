package com.example.webservice.controller;

import com.example.webservice.entity.User;
import com.example.webservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequestMapping(value = "/users")
public class UserController {
    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/signUp")
    public ResponseEntity<User> signUp(@Valid @RequestBody User user) {
        User createdUser = userRepository.save(user);
        //todo password encode
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestParam(name = "Username") String username, @RequestParam String password) {
      //  user.setLastLoginDate(LocalDate.now());
//        User createdUser = userRepository.findUserByUsername(username).get();
//        System.out.println(username+" "+password);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        return null;
    }
}
