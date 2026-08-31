package com.example.MarketDemo.service;
import com.example.MarketDemo.dto.DetalleVentaDTO;
import com.example.MarketDemo.dto.VentaDTO;
import com.example.MarketDemo.exception.NotFoundException;
import com.example.MarketDemo.mapper.Mapper;
import com.example.MarketDemo.model.DetalleVenta;
import com.example.MarketDemo.model.Producto;
import com.example.MarketDemo.model.Sucursal;
import com.example.MarketDemo.model.Venta;
import com.example.MarketDemo.repository.ProductoRepository;
import com.example.MarketDemo.repository.SucursalRepository;
import com.example.MarketDemo.repository.VentaRepository;
import com.example.MarketDemo.service.interfaces.IVentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepo;
    private final ProductoRepository produRepo;
    private final SucursalRepository sucuRepo;

    public VentaService(VentaRepository ventaRepo, ProductoRepository produRepo, SucursalRepository sucuRepo) {
        this.ventaRepo = ventaRepo;
        this.produRepo = produRepo;
        this.sucuRepo = sucuRepo;
    }

    @Override
    @Transactional
    public VentaDTO createVenta(VentaDTO newVenta) {

        //Validaciones previas
        if (newVenta == null) throw new RuntimeException("Venta es null");
        if (newVenta.getIdSucursal() == null) throw new RuntimeException("Debe indicar la sucursal");
        if (newVenta.getDetalle() == null || newVenta.getDetalle().isEmpty()) throw new RuntimeException("Debe incluir al menos un producto");

        // Buscar la sucursal
        Sucursal sucursal = sucuRepo.findById(newVenta.getIdSucursal())
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

        //Crear la venta con el detalle vacío: se llena abajo, enganchando los dos lados de la relación
        Venta venta = Venta.builder()
                .fecha(newVenta.getFecha())
                .estado(newVenta.getEstado())
                .sucursal(sucursal)
                .detalle(new ArrayList<>())
                .build();

        //La lista de detalles
        double total = 0.0;
        for (DetalleVentaDTO linea : newVenta.getDetalle()) {

            if (linea.getCantidad() == null || linea.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a cero");
            }

            Producto producto = produRepo.findByNombre(linea.getProductName())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + linea.getProductName()));

            //Se guarda el precio del momento de la venta; si no viene, se toma el actual del producto
            Double precio = linea.getPrecio() != null ? linea.getPrecio() : producto.getPrecio();

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantProd(linea.getCantidad())
                    .precio(precio)
                    .build();

            venta.getDetalle().add(detalle);
            total += precio * linea.getCantidad();
        }

        //El total se calcula acá, nunca se confía en el que manda el cliente
        venta.setTotal(total);

        return Mapper.toDTO(ventaRepo.save(venta));
    }

    @Override
    public List<VentaDTO> allVentas() {
        List<Venta> ventas = ventaRepo.findAll();
        List<VentaDTO> ventasDto = new ArrayList<>();

        VentaDTO dto;
        for (Venta v : ventas) {
            dto = Mapper.toDTO(v);
            ventasDto.add (dto);
        }

        return ventasDto;
    }

    @Override
    public VentaDTO updateVenta(Long id, VentaDTO dto) {

        Venta venta = ventaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        if (dto.getFecha()!=null){
            venta.setFecha(dto.getFecha());
        }

        if (dto.getEstado()!=null){
            venta.setEstado(dto.getEstado());
        }

        //El total no se actualiza a mano: sale del detalle, y este metodo no lo modifica.
        //Si alguna vez el update toca las lineas, hay que recalcularlo como en createVenta.

        if (dto.getIdSucursal()!=null){
            Sucursal sucursal = sucuRepo.findById(dto.getIdSucursal())
                    .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

            venta.setSucursal(sucursal);
        }

        return Mapper.toDTO(ventaRepo.save(venta));
    }

    @Override
    public void deleteVenta(Long id) {
        if (!ventaRepo.existsById(id)){
            throw new NotFoundException("Venta no encontrada");
        }

        ventaRepo.deleteById(id);
    }
}
