package com.app.controllers.dto;

import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.entities.groups.StatusTaskEnum;
import com.app.persistence.entities.users.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    private String tittle;
    private String description;
    private LocalDateTime initialDate;
    private LocalDateTime endDate;
    private UserDTO user;
    private GroupDTO group;
    private List<FileDTO> files;
    private StatusTaskEnum statusTask;
}
