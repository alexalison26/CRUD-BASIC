package com.hospital.pacientes.service;

import com.hospital.pacientes.DTO.habitacionDTO;
import com.hospital.pacientes.model.habitacion;
import com.hospital.pacientes.repository.Ihabitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class habitacionService {

    @Autowired
    private Ihabitacion ihabitacion;
    
    // Convertir entidad a DTO
    private habitacionDTO convertToDTO(habitacion entity) {
        return new habitacionDTO(
            entity.getId(),
            entity.getNumero(),
            entity.getDescripcion(),
            entity.getCapacidad()
        );
    }
    
    // Listar todas las habitaciones
    public List<habitacionDTO> getAllHabitaciones() {
        List<habitacion> lista = ihabitacion.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    // Obtener una habitación por id
    public habitacionDTO getHabitacionById(int id) {
        Optional<habitacion> opt = ihabitacion.findById(id);
        if(opt.isPresent()){
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Habitación no encontrada con id: " + id);
        }
    }
    
    // Crear una nueva habitación (se ignora el campo id para dejar que la BD lo genere)
    public habitacionDTO createHabitacion(habitacionDTO dto) {
        if(dto.getNumero() == null || dto.getNumero().trim().isEmpty()){
            throw new IllegalArgumentException("El número de habitación es obligatorio.");
        }
        if(dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()){
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if(dto.getCapacidad() <= 0){
            throw new IllegalArgumentException("La capacidad debe ser un número positivo.");
        }
        // Aquí ignoramos cualquier valor de id en dto
        habitacion entity = new habitacion();
        entity.setNumero(dto.getNumero());
        entity.setDescripcion(dto.getDescripcion());
        entity.setCapacidad(dto.getCapacidad());
        
        habitacion created = ihabitacion.save(entity);
        return convertToDTO(created);
    }
    
    // Actualizar una habitación existente
    public habitacionDTO updateHabitacion(int id, habitacionDTO dto) {
        if(dto.getNumero() == null || dto.getNumero().trim().isEmpty()){
            throw new IllegalArgumentException("El número de habitación es obligatorio.");
        }
        if(dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()){
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if(dto.getCapacidad() <= 0){
            throw new IllegalArgumentException("La capacidad debe ser un número positivo.");
        }
        Optional<habitacion> opt = ihabitacion.findById(id);
        if(!opt.isPresent()){
            throw new IllegalArgumentException("Habitación no encontrada con id: " + id);
        }
        habitacion entity = opt.get();
        entity.setNumero(dto.getNumero());
        entity.setDescripcion(dto.getDescripcion());
        entity.setCapacidad(dto.getCapacidad());
        habitacion updated = ihabitacion.save(entity);
        return convertToDTO(updated);
    }
    
    // Eliminar una habitación
    public void deleteHabitacion(int id) {
        if(!ihabitacion.existsById(id)) {
            throw new IllegalArgumentException("Habitación no encontrada con id: " + id);
        }
        ihabitacion.deleteById(id);
    }
}
