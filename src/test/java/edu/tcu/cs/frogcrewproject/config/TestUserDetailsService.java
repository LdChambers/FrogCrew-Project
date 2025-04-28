package edu.tcu.cs.frogcrewproject.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TestUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For testing purposes, create a test user with ROLE_USER
        return new User(
                "testuser",
                "{noop}password", // {noop} prefix means no password encoding
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}