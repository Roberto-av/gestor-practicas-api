package com.app.controllers.dto.request;

import com.app.controllers.dto.GroupDTO;
import com.app.persistence.entities.students.ShiftEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Data
public class StudentRequestDTO {

    private Long id;
    private int controlNumber;
    private String name;
    private String email;
    private String program;
    private int semester;
    private ShiftEnum shift;
    private GroupDTO group;
}
