package com.example.MarketDemo.controller;

import com.example.MarketDemo.dto.SucursalDTO;
import com.example.MarketDemo.service.SucursalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> allSucursales(){
        return ResponseEntity.ok(sucursalService.allSucursales());
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> createSucursal(@RequestBody SucursalDTO dto){
        SucursalDTO created = sucursalService.createSucursal(dto);
        return ResponseEntity
                .created(URI.create("/api/sucursales/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> updateSucursal(@PathVariable Long id, @RequestBody SucursalDTO dto){
        return ResponseEntity.ok(sucursalService.updateSucursal(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Long id){
        sucursalService.deleteSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
