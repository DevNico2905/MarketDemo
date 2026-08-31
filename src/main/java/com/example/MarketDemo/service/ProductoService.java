package com.example.MarketDemo.service;
import com.example.MarketDemo.dto.ProductoDTO;
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
        return null;
    }

    @Override
    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
