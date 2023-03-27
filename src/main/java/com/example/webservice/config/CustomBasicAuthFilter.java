package com.example.webservice.config;

import com.example.webservice.model.entity.User;
import com.example.webservice.model.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class CustomBasicAuthFilter extends BasicAuthenticationFilter {
    @Autowired
    private UserRepository userRepository;

    public CustomBasicAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    


    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            user.get().setLastLoginDate(LocalDate.now());
            userRepository.save(user.get());
        }
    }
}