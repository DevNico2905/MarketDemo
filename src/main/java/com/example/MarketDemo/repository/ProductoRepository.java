package com.example.MarketDemo.repository;

import com.example.MarketDemo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
