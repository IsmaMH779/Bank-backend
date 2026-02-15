package com.example.user_service.application.dto;

import com.example.user_service.domain.util.customAnnotations.PasswordsMatch;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
@PasswordsMatch
public record UserRegistrationDTO (
        @NotBlank(message = "Email field cannot be empty.")
        @Email(message = "Email format is not valid.")
        String email,

        @NotBlank(message = "Username field cannot be empty.")
        @Size(min = 3, max = 20,
                message = "Username must be between 3 and 20 characters.")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$",
                message = "The username can only contain letters, numbers, hyphens, and underscores.")
        String username,

        @NotBlank(message = "Password field cannot be empty.a")
        @Size(min = 8, message = "The passowrd must be at least 9 characters long.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "The password must contain at least one uppercase letter, one lowercase letter, and one number.")
        String password,

        @NotBlank(message = "You must re-enter the password.")
        String repeatPassword
) {}