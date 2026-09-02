package com.example.MarketDemo.controller;

import com.example.MarketDemo.dto.ProductoDTO;
import com.example.MarketDemo.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> allProducts(){
        return ResponseEntity.ok(productoService.allProducts());
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> createProduct(@RequestBody ProductoDTO producto){
        ProductoDTO created = productoService.createProducto(producto);
        return ResponseEntity.created(URI.create("api/productos" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProduct(@PathVariable Long id,
                                                     @RequestBody ProductoDTO dto){
        return ResponseEntity.ok(productoService.updateProducto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }
}
