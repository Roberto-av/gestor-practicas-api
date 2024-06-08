package com.app.controllers.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class SubmissionDTO {
    private Long id;
    private Long taskId;
    private Long userId;
    private Date submittedAt;
    private Date update_at;
    private List<SubmissionFileDTO> files;
}
