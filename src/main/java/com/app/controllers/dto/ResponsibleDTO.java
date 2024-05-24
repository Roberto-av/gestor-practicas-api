package com.app.controllers.dto;

import lombok.Data;

@Data
public class ResponsibleDTO {
    private Long id;
    private String name;
    private String position;
    private String email;
    private String confirmEmail;
    private String phone;
    private String education;
}
