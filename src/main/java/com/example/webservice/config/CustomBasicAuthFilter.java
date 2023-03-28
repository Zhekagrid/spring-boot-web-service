package com.example.webservice.config;

import com.example.webservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CustomBasicAuthFilter extends BasicAuthenticationFilter {
    @Autowired
    private UserService userService;

    public CustomBasicAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        userService.setUserLastLoginDate(authResult);
    }
}