package com.example.MarketDemo.service;
import com.example.MarketDemo.dto.ProductoDTO;
import com.example.MarketDemo.exception.NotFoundException;
import com.example.MarketDemo.mapper.Mapper;
import com.example.MarketDemo.model.Producto;
import com.example.MarketDemo.repository.ProductoRepository;
import com.example.MarketDemo.service.interfaces.IProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepo){
        this.productoRepository = productoRepo;
    }

    @Override
    public ProductoDTO createProducto(ProductoDTO newProduct) {

        //El nombre identifica al producto en el detalle de una venta, asi que no puede repetirse
        if (productoRepository.existsByNombre(newProduct.getNombre())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + newProduct.getNombre());
        }

        var product = Producto.builder()
                .nombre(newProduct.getNombre())
                .categoria(newProduct.getCategoria())
                .precio(newProduct.getPrecio())
                .cantidad(newProduct.getCantidad())
                .build();
        
        return Mapper.toDTO(productoRepository.save(product));
    }

    @Override
    public List<ProductoDTO> allProducts() {
        return productoRepository.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductoDTO updateProducto(Long id, ProductoDTO dto) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        //Renombrar tampoco puede pisar el nombre de otro producto
        productoRepository.findByNombre(dto.getNombre())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new RuntimeException("Ya existe un producto con el nombre: " + dto.getNombre());
                });

        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());

        return Mapper.toDTO(productoRepository.save(producto));
    }

    @Override
    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)){
            throw new NotFoundException("Producto no encontrado");
        }

        productoRepository.deleteById(id);
    }
}
