package com.app.exceptions;

public class textNotFundException extends RuntimeException {
    public textNotFundException(String text) {
        super("No se encontro un registro con el valor: " + text);
    }
}