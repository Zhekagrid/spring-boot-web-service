package com.example.webservice.service;

import com.example.webservice.dto.LoginInfoDto;
import com.example.webservice.entity.User;
import com.example.webservice.mapper.UserMapper;
import com.example.webservice.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public Optional<User> signUp(LoginInfoDto loginInfoDto) {
        Optional<User> optionalUser = Optional.empty();
        String username = loginInfoDto.getUsername();
        if (!userRepository.existsByUsername(username)) {
            User user = userMapper.loginDtoToUser(loginInfoDto);
            String password = user.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            optionalUser = Optional.of(userRepository.save(user));
        }
        return optionalUser;
    }
}
