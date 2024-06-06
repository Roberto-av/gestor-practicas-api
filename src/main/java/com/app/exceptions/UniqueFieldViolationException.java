package com.app.exceptions;

public class UniqueFieldViolationException extends RuntimeException {
    public UniqueFieldViolationException(String value) {
        super("El valor '" + value + "' ya existe en un registro");
    }
}
