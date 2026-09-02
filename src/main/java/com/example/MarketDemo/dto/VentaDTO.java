package com.example.MarketDemo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class VentaDTO {

    private final Long id;

    @NotNull(message = "La fecha es obligatoria")
    private final LocalDate fecha;

    @NotBlank(message = "El estado es obligatorio")
    private final String estado;

    //Solo de salida: en la entrada se ignora, el total lo recalcula el servidor desde el detalle
    private final Double total;

    @NotNull(message = "Debe indicar la sucursal")
    private final Long idSucursal;

    //@Valid propaga la validación a cada línea del detalle
    @NotEmpty(message = "La venta debe incluir al menos un producto")
    @Valid
    private final List<DetalleVentaDTO> detalle;
}
