package com.app.controllers.dto;

public record MailDTO(String[] toUser,
                      String subject,
                      String message) {
}
