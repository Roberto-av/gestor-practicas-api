package com.app.controllers.dto;

import jdk.jshell.Snippet;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private Long id;
    private String name;
    private String filePath;
    private Long taskId;

}
