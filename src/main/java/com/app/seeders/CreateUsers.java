package com.app.seeders;

import com.app.controllers.dto.StudentDTO;
import com.app.controllers.dto.initialData.RolePermissionDTO;
import com.app.controllers.dto.initialData.UserDTO;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.RoleRepository;
import com.app.persistence.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CreateUsers {
    private static final Logger logger = LoggerFactory.getLogger(CreateUsers.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper jacksonObjectMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateUsers(UserRepository userRepository, RoleRepository roleRepository,
                      ObjectMapper jacksonObjectMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createAllUsers() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/json/users.json")) {
            List<UserDTO> userDtos = jacksonObjectMapper.readValue(inputStream, new TypeReference<>() {});
            for (UserDTO userDto : userDtos) {
                Optional<UserEntity> existingUser = userRepository.findByUsername(userDto.getUsername());
                if (existingUser.isEmpty()) {
                    // Create new user entity
                    UserEntity user = new UserEntity();
                    user.setUsername(userDto.getUsername());
                    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    user.setEnabled(userDto.isEnabled());
                    user.setAccountNoExpired(userDto.isAccountNoExpired());
                    user.setAccountNoLocked(userDto.isAccountNoLocked());
                    user.setCredentialNoExpired(userDto.isCredentialNoExpired());

                    // Resolve roles from database
                    Set<RoleEntity> resolvedRoles = resolveRoles(userDto.getRoles());
                    user.setRoles(resolvedRoles);

                    // Set student information
                    if (userDto.getStudent() != null) {
                        StudentDTO studentDto = userDto.getStudent();
                        StudentEntity student = new StudentEntity();
                        student.setId(studentDto.getId());
                        user.setStudent(student);
                    }

                    // Save user
                    userRepository.save(user);
                    logger.info("User {} created successfully", user.getUsername());
                } else {
                    logger.info("User {} already exists. Skipping...", userDto.getUsername());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading users.json", e);
        }
    }

    private Set<RoleEntity> resolveRoles(Set<RolePermissionDTO> roleDtos) {
        Set<String> roleEnums = roleDtos.stream().map(RolePermissionDTO::getRoleEnum).collect(Collectors.toSet());
        List<RoleEntity> roles = roleRepository.findRoleEntitiesByRoleEnumIn(new ArrayList<>(roleEnums));
        if (roles.size() != roleEnums.size()) {
            Set<String> missingRoles = new HashSet<>(roleEnums);
            missingRoles.removeAll(roles.stream().map(role -> role.getRoleEnum().name()).collect(Collectors.toSet()));
            logger.error("The following roles are missing from the database: {}", missingRoles);
            throw new IllegalStateException("Missing roles: " + missingRoles);
        }
        return new HashSet<>(roles);
    }
}