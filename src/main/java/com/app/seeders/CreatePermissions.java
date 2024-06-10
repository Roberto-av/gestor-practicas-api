package com.app.seeders;

import com.app.persistence.entities.users.PermissionEntity;
import com.app.persistence.repositories.PermissionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
public class CreatePermissions {

    private static final Logger logger = LoggerFactory.getLogger(CreatePermissions.class);

    private final PermissionRepository permissionRepository;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public CreatePermissions(PermissionRepository permissionRepository, ObjectMapper jacksonObjectMapper) {
        this.permissionRepository = permissionRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public void createAllPermissions() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/json/permissions.json")) {
            List<PermissionEntity> permissions = jacksonObjectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (PermissionEntity permission : permissions) {
                Optional<PermissionEntity> existingPermission = Optional.ofNullable(permissionRepository.findByName(permission.getName()));
                if (existingPermission.isEmpty()) {
                    permissionRepository.save(permission);
                    logger.info("Permission {} created successfully", permission.getName());
                } else {
                    logger.info("Permission {} already exists. Skipping...", permission.getName());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading permissions.json", e);
        }
    }
}
