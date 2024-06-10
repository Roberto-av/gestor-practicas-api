package com.app.controllers.dto.initialData;

import lombok.Data;

import java.util.Set;

@Data
public class RolePermissionDTO {
    private String roleEnum;
    private Set<String> permissionList;
}
