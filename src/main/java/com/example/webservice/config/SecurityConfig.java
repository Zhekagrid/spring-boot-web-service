package com.example.webservice.config;

import com.example.webservice.model.entity.User;
import com.example.webservice.model.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Optional;

@Configuration
public class SecurityConfig {
    private static final String USERS_URL = "/users/*";
    private static final String AUTHORITY = "USER";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            Optional<User> optionalUser = userRepository.findUserByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
               //todo authorities
                return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword())
                        .authorities(AUTHORITY).build();
            }
            throw new UsernameNotFoundException("User: " + username + " not found");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter() throws Exception {
        return new CustomBasicAuthFilter(authenticationManager(new AuthenticationConfiguration()));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.addFilterAt(basicAuthenticationFilter(), BasicAuthenticationFilter.class).
                authorizeHttpRequests()
                .requestMatchers(USERS_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().and()
                .csrf().disable()
                .build();
    }
}

