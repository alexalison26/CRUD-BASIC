package com.hospital.pacientes.service;

import com.hospital.pacientes.model.factura;
import com.hospital.pacientes.model.paciente;
import com.hospital.pacientes.DTO.facturaDTO;
import com.hospital.pacientes.repository.Ifactura;
import com.hospital.pacientes.repository.Ipaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class facturaService {

    @Autowired
    private Ifactura ifactura;
    
    @Autowired
    private Ipaciente ipaciente;  // Repositorio de pacientes

    // Conversión de entidad a DTO
    private facturaDTO convertToDTO(factura entity) {
        return new facturaDTO(
            entity.getId(),
            entity.getPaciente().getId(),
            entity.getPaciente().getNombre(), // Aquí mostramos el nombre del paciente
            entity.getTotal(),
            entity.getFechaPago()
        );
    }
    
    // Conversión de DTO a entidad – se usa el idPaciente para obtener el objeto paciente
    private factura convertToEntity(facturaDTO dto) {
        factura entity = new factura();
        entity.setId(dto.getId());
        
        // Buscar el paciente a partir del id enviado en el DTO
        paciente p = ipaciente.findById(dto.getIdPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con id: " + dto.getIdPaciente()));
        entity.setPaciente(p);
        entity.setTotal(dto.getTotal());
        entity.setFechaPago(dto.getFechaPago());
        return entity;
    }
    
    public List<facturaDTO> getAllFacturas() {
        List<factura> list = ifactura.findAll();
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public facturaDTO getFacturaById(int id) {
        Optional<factura> opt = ifactura.findById(id);
        if(opt.isPresent()){
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Factura no encontrada con id: " + id);
        }
    }
    
    public facturaDTO createFactura(facturaDTO dto) {
        if(dto.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("El paciente es obligatorio y debe ser válido.");
        }
        if(dto.getTotal() < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo.");
        }
        if(dto.getFechaPago() == null || dto.getFechaPago().trim().isEmpty()){
            throw new IllegalArgumentException("La fecha de pago es obligatoria.");
        }
        factura entity = convertToEntity(dto);
        factura created = ifactura.save(entity);
        return convertToDTO(created);
    }
    
    public facturaDTO updateFactura(int id, facturaDTO dto) {
        if(dto.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("El paciente es obligatorio y debe ser válido.");
        }
        if(dto.getTotal() < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo.");
        }
        if(dto.getFechaPago() == null || dto.getFechaPago().trim().isEmpty()){
            throw new IllegalArgumentException("La fecha de pago es obligatoria.");
        }
        Optional<factura> opt = ifactura.findById(id);
        if(!opt.isPresent()){
            throw new IllegalArgumentException("Factura no encontrada con id: " + id);
        }
        factura entity = opt.get();
        // Buscamos el paciente
        paciente p = ipaciente.findById(dto.getIdPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con id: " + dto.getIdPaciente()));
        entity.setPaciente(p);
        entity.setTotal(dto.getTotal());
        entity.setFechaPago(dto.getFechaPago());
        factura updated = ifactura.save(entity);
        return convertToDTO(updated);
    }
    
    public void deleteFactura(int id) {
        if(!ifactura.existsById(id)){
            throw new IllegalArgumentException("Factura no encontrada con id: " + id);
        }
        ifactura.deleteById(id);
    }
}
