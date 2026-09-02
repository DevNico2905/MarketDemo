package com.example.MarketDemo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SucursalDTO {

    private final Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private final String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private final String direccion;

    public SucursalDTO(Long id, String nombre, String direccion){
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }
}
