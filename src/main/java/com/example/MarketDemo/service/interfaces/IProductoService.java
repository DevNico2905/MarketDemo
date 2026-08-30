package com.example.MarketDemo.service.interfaces;
import com.example.MarketDemo.dto.ProductoDTO;
import java.util.List;

public interface IProductoService {

    //CRUD

    //C - create
    ProductoDTO createProducto(ProductoDTO newProduct);

    //R - read
    List<ProductoDTO> allProducts();

    //U - updated
    ProductoDTO updateProducto(Long id, ProductoDTO dto);

    //D - delete
    void deleteProducto(Long id);
}
