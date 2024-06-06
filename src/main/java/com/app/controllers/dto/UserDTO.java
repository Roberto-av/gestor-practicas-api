package com.app.controllers.dto;

import com.app.persistence.entities.teachers.TeacherEntity;
import com.app.persistence.entities.users.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private TeacherEntity teacher;
    private StudentDTO student;
    private Set<RoleDTO> roles;
    private Date createdAt;
    private Date updatedAt;
}
