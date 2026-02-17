package com.habitflow.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserService {

    public String getEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}