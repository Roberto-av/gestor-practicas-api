package com.app.controllers.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateStudentUser(@NotBlank String username, @NotBlank String password) {
}
