package com.example.MarketDemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private final Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private final String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    private final String categoria;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private final Double precio;

    //Integer y no int: con el primitivo, un campo ausente moría en un error crudo de Jackson
    //antes de llegar a la validación
    @NotNull(message = "La cantidad es obligatoria")
    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    private final Integer cantidad;
}
