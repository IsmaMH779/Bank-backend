package com.example.auth_service.application;

import com.example.auth_service.adapters.out.UserRepository;
import com.example.auth_service.application.dto.UserRegistrationRequest;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.domain.util.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void newCustomerRegistration(UserRegistrationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())
                || userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("User already exists");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .profileImage("")
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.CUSTOMER)
                .build();

        userRepository.save(newUser);
    }
}
