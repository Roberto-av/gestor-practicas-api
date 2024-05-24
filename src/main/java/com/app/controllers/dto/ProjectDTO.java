package com.app.controllers.dto;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String schedule;
    private String justification;
    private String objectives;
    private String functions;
    private int numHours;
    private int requested;
    private int registered;
    private Long institutionId;
}
