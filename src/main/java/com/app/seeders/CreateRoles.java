package com.app.seeders;

import com.app.controllers.dto.initialData.RolePermissionDTO;
import com.app.persistence.entities.users.PermissionEntity;
import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.entities.users.RoleEnum;
import com.app.persistence.repositories.PermissionRepository;
import com.app.persistence.repositories.RoleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CreateRoles {
    private static final Logger logger = LoggerFactory.getLogger(CreateRoles.class);

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public CreateRoles(RoleRepository roleRepository, PermissionRepository permissionRepository, ObjectMapper jacksonObjectMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Transactional
    public void createAllRoles() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/json/roles.json")) {
            List<RolePermissionDTO> roleDtos = jacksonObjectMapper.readValue(inputStream, new TypeReference<>() {});
            for (RolePermissionDTO roleDto : roleDtos) {
                Optional<RoleEntity> existingRole = Optional.ofNullable(roleRepository.findByRoleEnum(RoleEnum.valueOf(roleDto.getRoleEnum())));
                if (existingRole.isEmpty()) {
                    // Create new role entity
                    RoleEntity role = new RoleEntity();
                    role.setRoleEnum(RoleEnum.valueOf(roleDto.getRoleEnum()));

                    // Resolve permissions from database
                    Set<PermissionEntity> resolvedPermissions = resolvePermissions(roleDto.getPermissionList());
                    role.setPermissionList(resolvedPermissions);

                    roleRepository.save(role);
                    logger.info("Role {} created successfully", role.getRoleEnum());
                } else {
                    logger.info("Role {} already exists. Skipping...", roleDto.getRoleEnum());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading roles.json", e);
        }
    }


    private Set<PermissionEntity> resolvePermissions(Set<String> permissionNames) {
        List<PermissionEntity> permissions = permissionRepository.findAllByNameIn(permissionNames);
        if (permissions.size() != permissionNames.size()) {
            Set<String> missingPermissions = new HashSet<>(permissionNames);
            missingPermissions.removeAll(permissions.stream().map(PermissionEntity::getName).collect(Collectors.toSet()));
            logger.error("The following permissions are missing from the database: {}", missingPermissions);
            throw new IllegalStateException("Missing permissions: " + missingPermissions);
        }
        return new HashSet<>(permissions);
    }
}
