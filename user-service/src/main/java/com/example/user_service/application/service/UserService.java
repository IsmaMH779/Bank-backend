package com.example.user_service.application.service;

import com.example.user_service.application.dto.UserRegistrationDTO;
import com.example.user_service.application.dto.UserResponseDTO;

public interface UserService {
    void newUser(UserRegistrationDTO userReg);
    UserResponseDTO findById(Long userId);
    UserResponseDTO findMe();
}