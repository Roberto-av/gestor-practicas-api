package com.app.controllers.dto;

import lombok.*;

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
    private Date initialDate;
    private Date endDate;
    private Long userId;
    private Long groupId;
    private List<FileDTO> files;
}
