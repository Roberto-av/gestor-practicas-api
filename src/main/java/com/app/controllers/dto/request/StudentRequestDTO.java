package com.app.controllers.dto.request;

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

    private int controlNumber;
    private String name;
    private String email;
    private String program;
    private String semester;
    private ShiftEnum shift;
}
