package com.example.MarketDemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DetalleVentaDTO {

    private final Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private final String productName;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private final Integer cantidad;

    //Opcional en la entrada: si no viene se toma el precio actual del producto
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private final Double precio;

    private final Double subtotal;
}
