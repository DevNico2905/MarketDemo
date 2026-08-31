package com.example.MarketDemo.service;
import com.example.MarketDemo.dto.SucursalDTO;
import com.example.MarketDemo.mapper.Mapper;
import com.example.MarketDemo.model.Sucursal;
import com.example.MarketDemo.repository.SucursalRepository;
import com.example.MarketDemo.service.interfaces.ISucursalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService implements ISucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalService(SucursalRepository sucuRepo){
        this.sucursalRepository = sucuRepo;
    }

    @Override
    public List<SucursalDTO> allSucursales() {
        return sucursalRepository.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public SucursalDTO createSucursal(SucursalDTO newDto) {

        Sucursal sucursal = Sucursal.builder()
                .nombre(newDto.getNombre())
                .direccion(newDto.getDireccion())
                .build();

        return Mapper.toDTO(sucursalRepository.save(sucursal));
    }

    @Override
    public SucursalDTO updateSucursal(Long id, SucursalDTO dto) {
        return null;
    }

    @Override
    public void deleteSucursal(Long id) {

    }
}
