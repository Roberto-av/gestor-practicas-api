package com.app.controllers.dto;

import com.app.persistence.entities.groups.TaskEntity;
import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {

    private Long id;
    private String name;
    private String description;
    private List<TaskDTO> tasks;
}
