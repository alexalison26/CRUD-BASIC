package com.hospital.pacientes.service;

import com.hospital.pacientes.model.enfermero;
import com.hospital.pacientes.DTO.enfermeroDTO;
import com.hospital.pacientes.repository.Ienfermero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class enfermeroService {

    @Autowired
    private Ienfermero ienfermero;

    // Conversión de entidad a DTO
    private enfermeroDTO convertToDTO(enfermero entity) {
        return new enfermeroDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getTurno(),
                entity.getAniosExperiencia()
        );
    }

    // Conversión de DTO a entidad
    private enfermero convertToEntity(enfermeroDTO dto) {
        enfermero entity = new enfermero();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setTurno(dto.getTurno());
        entity.setAniosExperiencia(dto.getAniosExperiencia());
        return entity;
    }

    // Listar todos los enfermeros
    public List<enfermeroDTO> getAllEnfermeros() {
        List<enfermero> lista = ienfermero.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener enfermero por ID
    public enfermeroDTO getEnfermeroById(int id) {
        Optional<enfermero> opt = ienfermero.findById(id);
        if (opt.isPresent()) {
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Enfermero no encontrado con id: " + id);
        }
    }

    // Crear un nuevo enfermero
    public enfermeroDTO createEnfermero(enfermeroDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getTurno() == null || dto.getTurno().trim().isEmpty()) {
            throw new IllegalArgumentException("El turno es obligatorio.");
        }
        // Validamos que los años de experiencia sean >= 0
        if (dto.getAniosExperiencia() < 0) {
            throw new IllegalArgumentException("Los años de experiencia deben ser 0 o mayores.");
        }
        enfermero entity = convertToEntity(dto);
        enfermero created = ienfermero.save(entity);
        return convertToDTO(created);
    }

    // Actualizar un enfermero existente
    public enfermeroDTO updateEnfermero(int id, enfermeroDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getTurno() == null || dto.getTurno().trim().isEmpty()) {
            throw new IllegalArgumentException("El turno es obligatorio.");
        }
        if (dto.getAniosExperiencia() < 0) {
            throw new IllegalArgumentException("Los años de experiencia deben ser 0 o mayores.");
        }
        Optional<enfermero> opt = ienfermero.findById(id);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("Enfermero no encontrado con id: " + id);
        }
        enfermero entity = opt.get();
        entity.setNombre(dto.getNombre());
        entity.setTurno(dto.getTurno());
        entity.setAniosExperiencia(dto.getAniosExperiencia());
        enfermero updated = ienfermero.save(entity);
        return convertToDTO(updated);
    }

    // Eliminar un enfermero
    public void deleteEnfermero(int id) {
        if (!ienfermero.existsById(id)) {
            throw new IllegalArgumentException("Enfermero no encontrado con id: " + id);
        }
        ienfermero.deleteById(id);
    }
    
    // Filtrar enfermeros: por nombre y turno (opcional)
    public List<enfermeroDTO> filterEnfermeros(String nombre, String turno) {
        List<enfermero> lista = ienfermero.findAll();
        return lista.stream().filter(e -> {
            boolean matchNombre = true;
            boolean matchTurno = true;
            if (nombre != null && !nombre.trim().isEmpty()) {
                matchNombre = e.getNombre().toLowerCase().contains(nombre.toLowerCase());
            }
            if (turno != null && !turno.trim().isEmpty()) {
                matchTurno = e.getTurno().toLowerCase().contains(turno.toLowerCase());
            }
            return matchNombre && matchTurno;
        }).map(this::convertToDTO).collect(Collectors.toList());
    }
}
