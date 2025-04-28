package edu.tcu.cs.frogcrewproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@Profile("!test")
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // For now, we'll create a simple user with admin and user roles
            // In a real application, this would load the user from a database
            return new User(
                    "admin",
                    passwordEncoder().encode("admin"),
                    Arrays.asList(
                            new SimpleGrantedAuthority("ROLE_admin"),
                            new SimpleGrantedAuthority("ROLE_USER")));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}