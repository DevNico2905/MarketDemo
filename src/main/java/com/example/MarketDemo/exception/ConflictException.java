package com.example.MarketDemo.exception;

/**
 * El pedido es válido, pero choca con el estado actual de los datos:
 * un nombre de producto repetido, o un borrado que rompería una relación.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String msj) {
        super(msj);
    }
}
