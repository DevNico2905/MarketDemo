package com.example.MarketDemo.exception;

/**
 * Datos de entrada inválidos que no alcanza a filtrar Bean Validation,
 * porque dependen de una regla de negocio.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String msj) {
        super(msj);
    }
}
