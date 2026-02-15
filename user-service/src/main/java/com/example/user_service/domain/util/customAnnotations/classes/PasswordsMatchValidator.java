package com.example.user_service.domain.util.customAnnotations.classes;

import com.example.user_service.application.dto.UserRegistrationDTO;
import com.example.user_service.domain.util.customAnnotations.PasswordsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationDTO> {

    @Override
    public boolean isValid(UserRegistrationDTO userReg, ConstraintValidatorContext context) {

        if (userReg == null) {
            return true;
        }

        // Verify that both fields are equal
        return userReg.password().equals(userReg.repeatPassword());
    }
}