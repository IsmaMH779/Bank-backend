package com.example.auth_service.adapters.in;

import com.example.auth_service.application.dto.UserRegistrationRequest;
import com.example.auth_service.application.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserRegistrationController {

    private UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerUser (@Valid @RequestBody UserRegistrationRequest request) {
        userRegistrationService.newCustomerRegistration(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
