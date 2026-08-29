package com.example.MarketDemo.repository;

import com.example.MarketDemo.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
