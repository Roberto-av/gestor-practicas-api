package com.app.controllers.dto;

import com.app.persistence.entities.students.ShiftEnum;
import lombok.Data;

import java.util.Date;

@Data
public class StudentDTO {
    private Long id;
    private int controlNumber;
    private String name;
    private String email;
    private String program;
    private int semester;
    private ShiftEnum shift;
    private GroupDTO group;
    private Date createdAt;
    private Date updatedAt;
    private String username;
    private InstitutionDTO institution;
}
