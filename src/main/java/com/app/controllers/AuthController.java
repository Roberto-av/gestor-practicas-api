package com.app.controllers;

import com.app.controllers.dto.request.AuthCreateStudentUser;
import com.app.controllers.dto.request.AuthCreateUserRequest;
import com.app.controllers.dto.request.AuthLoginRequest;
import com.app.controllers.dto.response.AuthResponse;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.users.InvitationTokenEntity;
import com.app.persistence.repositories.InvitationTokenRepository;
import com.app.persistence.repositories.RoleRepository;
import com.app.persistence.repositories.StudentRepository;
import com.app.services.impl.UserDetailsServiceImpl;
import com.app.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private InvitationTokenRepository invitationTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest authCreateUserRequest) {
        if (!isValidRoles(authCreateUserRequest.roleRequest().roles())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Invalid roles", null, false));
        }

        try {
            return new ResponseEntity<>(this.userDetailsService.createUser(authCreateUserRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, e.getMessage(), null, false));
        }
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestParam("token") String token,
                                             @RequestBody @Valid AuthCreateStudentUser registerUserRequest) {
        try {
            Optional<InvitationTokenEntity> optionalToken = invitationTokenRepository.findByToken(token);
            if (optionalToken.isEmpty() || optionalToken.get().isUsed() || optionalToken.get().getExpiryDate().before(new Date())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            InvitationTokenEntity invitationToken = optionalToken.get();
            String email = invitationToken.getEmail();
            Long groupId = invitationToken.getGroup().getId();

            Optional<StudentEntity> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                StudentEntity student = studentOptional.get();

                userDetailsService.createUserWithRole(email, registerUserRequest.username(), registerUserRequest.password(), "STUDENT");
                student.setGroup(invitationToken.getGroup());
                studentRepository.save(student);

                invitationToken.setUsed(true);
                invitationTokenRepository.save(invitationToken);

                return ResponseEntity.ok("Registro completado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se encontró el estudiante con el email especificado");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    private boolean isValidRoles(List<String> roles) {
        List<String> validRoles = roleRepository.findAll().stream()
                .map(roleEntity -> roleEntity.getRoleEnum().name())
                .collect(Collectors.toList());

        return roles.stream().allMatch(validRoles::contains);
    }
}
