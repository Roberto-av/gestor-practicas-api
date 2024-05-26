package com.app.controllers.dto;

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
    private Long userId;
    private Long groupId;
    private List<FileDTO> files;
}
