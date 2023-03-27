package com.example.webservice.service.impl;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.ErrorInfo;
import com.example.webservice.model.dto.LoginInfoDto;
import com.example.webservice.model.entity.User;
import com.example.webservice.model.repository.UserRepository;
import com.example.webservice.service.UserService;
import com.example.webservice.utill.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_LOGIN_UNIQUE = "Login must be unique";
    private static final String USER_SIGNED_SUCCESS = "User signed-in successfully!.";

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<? extends BaseDto> signUp(LoginInfoDto loginInfoDto) {
        String username = loginInfoDto.getUsername();
        if (!userRepository.existsByUsername(username)) {
            User user = userMapper.loginDtoToUser(loginInfoDto);
            String password = user.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            ErrorInfo errorInfo = new ErrorInfo(USER_LOGIN_UNIQUE, 400);
            return new ResponseEntity<>(new BaseDto(errorInfo), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<String> signIn(LoginInfoDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(USER_SIGNED_SUCCESS, HttpStatus.OK);
    }

}
