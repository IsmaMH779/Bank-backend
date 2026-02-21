package com.example.user_service.application.service.impl;

import com.example.user_service.adapters.out.UserRepository;
import com.example.user_service.application.dto.UserRegistrationDTO;
import com.example.user_service.application.dto.UserResponseDTO;
import com.example.user_service.application.service.UserService;
import com.example.user_service.domain.exception.ObjectNotFoundException;
import com.example.user_service.domain.model.User;
import com.example.user_service.domain.util.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void newUser(UserRegistrationDTO userReg) {

        userRepository.findByEmail(userReg.email())
                .ifPresent(user -> {
                    throw new ObjectNotFoundException("User already exists with this email: " + user.getEmail());
                });

        userRepository.findByUsername(userReg.username())
                .ifPresent(user -> {
                    throw new ObjectNotFoundException("User already exists with this username: " + user.getUsername());
                });

        User newUser = User.builder()
                .username(userReg.username())
                .password(passwordEncoder.encode(userReg.password()))
                .email(userReg.email())
                .role(RoleEnum.CUSTOMER)
                .build();

        userRepository.save(newUser);

    }

    @Override
    public UserResponseDTO findById(Long userId) {

        User userFromDB = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User do not exist with ID: " + userId));

        return UserResponseDTO.builder()
                .username(userFromDB.getUsername())
                .email(userFromDB.getEmail())
                .profilePicture(userFromDB.getProfileImage())
                .build();
    }

    @Override
    public UserResponseDTO findMe() {

        // get user id from securityContextHolder
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("user_id");

        return findById(userId);
    }
}
