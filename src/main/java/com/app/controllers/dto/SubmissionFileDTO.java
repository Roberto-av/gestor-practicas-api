package com.app.controllers.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SubmissionFileDTO {
    private Long id;
    private String name;
    private String filePath;
    private Date submittedAt;
}
