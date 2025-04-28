package com.hospital.pacientes.service;

import com.hospital.pacientes.DTO.tratamientoDTO;
import com.hospital.pacientes.model.tratamiento;
import com.hospital.pacientes.repository.Itratamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class tratamientoService {

    @Autowired
    private Itratamiento itratamiento;
    
    // Conversión de entidad a DTO
    private tratamientoDTO convertToDTO(tratamiento entity) {
        return new tratamientoDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion()
        );
    }
    
    // Listar todos los tratamientos
    public List<tratamientoDTO> getAllTratamientos() {
        List<tratamiento> lista = itratamiento.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    // Obtener tratamiento por id
    public tratamientoDTO getTratamientoById(int id) {
        Optional<tratamiento> opt = itratamiento.findById(id);
        if(opt.isPresent()){
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Tratamiento no encontrado con id: " + id);
        }
    }
    
    // Crear un nuevo tratamiento
    public tratamientoDTO createTratamiento(tratamientoDTO dto) {
        if(dto.getNombre() == null || dto.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if(dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()){
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        // Ignoramos el campo id para que se genere automáticamente.
        tratamiento entity = new tratamiento();
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        
        tratamiento created = itratamiento.save(entity);
        return convertToDTO(created);
    }
    
    // Actualizar tratamiento existente
    public tratamientoDTO updateTratamiento(int id, tratamientoDTO dto) {
        if(dto.getNombre() == null || dto.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if(dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()){
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        Optional<tratamiento> opt = itratamiento.findById(id);
        if(!opt.isPresent()){
            throw new IllegalArgumentException("Tratamiento no encontrado con id: " + id);
        }
        tratamiento entity = opt.get();
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        tratamiento updated = itratamiento.save(entity);
        return convertToDTO(updated);
    }
    
    // Eliminar tratamiento
    public void deleteTratamiento(int id) {
        if(!itratamiento.existsById(id)){
            throw new IllegalArgumentException("Tratamiento no encontrado con id: " + id);
        }
        itratamiento.deleteById(id);
    }
}
