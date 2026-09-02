package com.example.MarketDemo.controller;

import com.example.MarketDemo.dto.VentaDTO;
import com.example.MarketDemo.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> allVentas(){
        return ResponseEntity.ok(ventaService.allVentas());
    }

    @PostMapping
    public ResponseEntity<VentaDTO> createVenta(@Valid @RequestBody VentaDTO dto){
        VentaDTO newVenta = ventaService.createVenta(dto);
        return ResponseEntity.created(URI.create("/api/ventas/" + newVenta.getId())).body(newVenta);
    }

    //Sin @Valid a propósito: acá el update es parcial (el servicio solo pisa los campos que vienen),
    //así que las restricciones de VentaDTO, pensadas para el alta, no aplican.
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> updateVenta(@PathVariable Long id, @RequestBody VentaDTO dto){
        return ResponseEntity.ok(ventaService.updateVenta(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id){
        ventaService.deleteVenta(id);
        return ResponseEntity.noContent().build();
    }
}
