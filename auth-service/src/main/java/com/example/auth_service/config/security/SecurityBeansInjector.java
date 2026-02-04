package com.example.auth_service.config.security;

import com.example.auth_service.adapters.out.UserRepository;
import com.example.auth_service.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class SecurityBeansInjector {

    private UserRepository userRepostiory;

    @Autowired
    public SecurityBeansInjector(UserRepository userRepostiory) {
        this.userRepostiory = userRepostiory;
    }

    @Bean
    public AuthenticationProvider authenticationProvider () {
        DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider(userDetailsService());
        authenticationStrategy.setPasswordEncoder(passwordEncoder());

        return authenticationStrategy;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        return identifier -> {

            // try with email
            Optional<User> user = userRepostiory.findByEmail(identifier);

            // if not found try with username
            if (user.isEmpty()) user = userRepostiory.findByUsername(identifier);

            // if not found try with id
            if (user.isEmpty() && identifier.matches("\\d+")) {
                user = userRepostiory.findById(Long.parseLong(identifier));
            }

            return user
                    .orElseThrow(() -> new UsernameNotFoundException("UserNotFound"));
        };
    }
}
