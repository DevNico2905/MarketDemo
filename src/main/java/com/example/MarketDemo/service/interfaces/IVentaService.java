package com.example.MarketDemo.service.interfaces;
import com.example.MarketDemo.dto.VentaDTO;
import java.util.List;

public interface IVentaService {

    //CRUD

    //C - create
    VentaDTO createVenta(VentaDTO newVenta);

    //R - read
    List<VentaDTO> allVentas();

    //U - update
    VentaDTO updateVenta(Long id, VentaDTO dto);

    //D - delete
    void deleteVenta(Long id);
}
