package com.example.MarketDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private final Long id;
    private final String nombre;
    private final String categoria;
    private final Double precio;
    private final int cantidad;
}

