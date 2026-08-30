package com.example.MarketDemo.mapper;

import com.example.MarketDemo.dto.ProductoDTO;
import com.example.MarketDemo.dto.SucursalDTO;
import com.example.MarketDemo.dto.VentaDTO;
import com.example.MarketDemo.model.Producto;
import com.example.MarketDemo.model.Sucursal;
import com.example.MarketDemo.model.Venta;

import java.util.List;

public class Mapper {

    //Mapeo de Producto a ProductoDTO
    public static ProductoDTO toDTO(Producto producto){
        if (producto == null) return null;

        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .build();
    }

    //Mapeo de Venta a VentaDTO
    public static VentaDTO toDTO(Venta v){
        if (v == null) return null;

        return VentaDTO.builder()
                .id(v.getId())
                .fecha(v.getFecha())
                .estado(v.getEstado())
                .total(v.getTotal())
                .idSucursal(v.getSucursal().getId())
                .detalle(detalle)
                .build()

    }


    //Mapeo de Sucursal a SucursalDTO
    public static SucursalDTO toDTO(Sucursal s) {
        if (s == null) return null;
        return SucursalDTO.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .build();
    }
}
