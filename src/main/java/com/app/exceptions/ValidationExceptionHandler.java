package com.app.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        result.getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleUniqueConstraintException(DataIntegrityViolationException ex) {
        String errorMessage =  extractColumnName(ex) + " Ya existe";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    private String extractColumnName(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        // Analiza el mensaje de la excepción para extraer el nombre de la columna única
        // Esto puede variar según el mensaje específico de tu proveedor de base de datos
        // Aquí hay un ejemplo para MySQL
        if (message.contains("Duplicate entry")) {
            int start = message.indexOf("for key '") + 9; // 'for key ' tiene longitud 9
            int end = message.indexOf("'", start);
            return message.substring(start, end);
        }
        // Si no se puede extraer el nombre de la columna, regresa un mensaje genérico
        return "campo único";
    }
}
