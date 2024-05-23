package com.app.controllers.dto;

import org.springframework.web.multipart.MultipartFile;


public record MailFileDTO(String[] toUser,
                          String subject,
                          String message,
                          MultipartFile file) {
}
