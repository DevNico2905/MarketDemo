package com.example.MarketDemo.service;
import com.example.MarketDemo.dto.VentaDTO;
import com.example.MarketDemo.service.interfaces.IVentaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService implements IVentaService {
    @Override
    public VentaDTO createVenta(VentaDTO newVenta) {
        return null;
    }

    @Override
    public List<VentaDTO> allVentas() {
        return List.of();
    }

    @Override
    public VentaDTO updateVenta(Long id, VentaDTO dto) {
        return null;
    }

    @Override
    public void deleteVenta(Long id) {

    }
}
