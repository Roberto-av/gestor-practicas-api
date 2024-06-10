package com.app.controllers.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private Long id;
    private String name;
    private String filePath;
    private Long taskId;
    private Date createdAt;
    private Date updatedAt;
}
