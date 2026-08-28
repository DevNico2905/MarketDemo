package com.example.MarketDemo.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class VentaDTO {

    private final Long id;
    private final LocalDate fecha;
    private final String estado;
    private final Double total;
    private final Long idSucursal;
    private final List<DetalleVentaDTO> detalle;
}
