package com.db.weather_app.exceptions;

public class DuplicidadeClimaException extends RuntimeException {

    public DuplicidadeClimaException() {
        super("Registro duplicado");
    }

    public DuplicidadeClimaException(String message) {
        super(message);
    }
}
