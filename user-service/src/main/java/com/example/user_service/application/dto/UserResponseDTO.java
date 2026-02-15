package com.example.user_service.application.dto;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        String username,
        String email,
        String profilePicture
) {}
