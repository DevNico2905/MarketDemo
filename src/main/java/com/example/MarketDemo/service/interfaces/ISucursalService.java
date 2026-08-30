package com.example.MarketDemo.service.interfaces;
import com.example.MarketDemo.dto.SucursalDTO;
import java.util.List;

public interface ISucursalService {

    List<SucursalDTO> allSucursales();

    SucursalDTO createSucursal(SucursalDTO newDto);

    SucursalDTO updateSucursal(Long id, SucursalDTO dto);

    void deleteSucursal(Long id);
}
