package com.app.controllers.dto.initialData;

import com.app.controllers.dto.StudentDTO;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private String username;
    private String password;
    private boolean isEnabled;
    private boolean accountNoExpired;
    private boolean accountNoLocked;
    private boolean credentialNoExpired;
    private Set<RolePermissionDTO> roles;
    private StudentDTO student;
}
