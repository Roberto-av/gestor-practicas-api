package com.app.exceptions;

public class IdNotFundException extends RuntimeException {
    public IdNotFundException(Long id) {
        super("No se encontró un registro con el ID: " + id);
    }
}