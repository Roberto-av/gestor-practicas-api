package com.app.controllers;

import com.app.controllers.dto.request.AuthCreateUserRequest;
import com.app.controllers.dto.request.AuthLoginRequest;
import com.app.controllers.dto.response.AuthResponse;
import com.app.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthCreateUserRequest authCreateUserRequest) {
        // Validar si los roles son válidos
        if (!isValidRoles(authCreateUserRequest.roleRequest().roles())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Roles no validos", null, false));
        }

        return new ResponseEntity<>(this.userDetailsService.createUser(authCreateUserRequest), HttpStatus.OK);
    }

    private boolean isValidRoles(List<String> roles) {
        List<String> validRoles = Arrays.asList("ADMIN", "TEACHER", "STUDENT", "DEVELOPER");
        return roles.stream().allMatch(validRoles::contains);
    }
}
