package com.example.MarketDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetalleVentaDTO {

    private final Long id;
    private final String productName;
    private final Integer cantidad;
    private final Double precio;
    private final Double subtotal;
}
