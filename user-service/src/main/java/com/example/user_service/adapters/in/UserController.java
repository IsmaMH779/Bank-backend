package com.example.user_service.adapters.in;

import com.example.user_service.application.dto.UserRegistrationDTO;
import com.example.user_service.application.dto.UserResponseDTO;
import com.example.user_service.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createNewUser (@RequestBody @Valid UserRegistrationDTO userReg) {

        userService.newUser(userReg);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> findById (@PathVariable Long userId) {

        UserResponseDTO userResp = userService.findById(userId);

        return ResponseEntity.ok(userResp);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile () {

        UserResponseDTO userResp = userService.findMe();

        return ResponseEntity.ok(userResp);
    }
}
