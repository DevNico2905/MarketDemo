package com.example.MarketDemo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SucursalDTO {

    private final Long id;
    private final String nombre, direccion;

    public SucursalDTO(Long id, String nombre, String direccion){
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    
}
