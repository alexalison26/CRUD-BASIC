package com.hospital.pacientes.service;

import com.hospital.pacientes.model.departamento;
import com.hospital.pacientes.DTO.departamentoDTO;
import com.hospital.pacientes.repository.Idepartamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class departamentoService {

    @Autowired
    private Idepartamento idepartamento;

    // Conversión de entidad a DTO
    private departamentoDTO convertToDTO(departamento entity) {
        return new departamentoDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getUbicacion()
        );
    }

    // Conversión de DTO a entidad
    private departamento convertToEntity(departamentoDTO dto) {
        departamento entity = new departamento();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setUbicacion(dto.getUbicacion());
        return entity;
    }

    // Listar todos los departamentos
    public List<departamentoDTO> getAllDepartamentos() {
        List<departamento> lista = idepartamento.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener departamento por ID
    public departamentoDTO getDepartamentoById(int id) {
        Optional<departamento> opt = idepartamento.findById(id);
        if (opt.isPresent()) {
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Departamento no encontrado con id: " + id);
        }
    }

    // Crear un nuevo departamento
    public departamentoDTO createDepartamento(departamentoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getUbicacion() == null || dto.getUbicacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación es obligatoria.");
        }
        departamento entity = convertToEntity(dto);
        departamento created = idepartamento.save(entity);
        return convertToDTO(created);
    }

    // Actualizar un departamento existente
    public departamentoDTO updateDepartamento(int id, departamentoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getUbicacion() == null || dto.getUbicacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación es obligatoria.");
        }
        Optional<departamento> opt = idepartamento.findById(id);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("Departamento no encontrado con id: " + id);
        }
        departamento entity = opt.get();
        entity.setNombre(dto.getNombre());
        entity.setUbicacion(dto.getUbicacion());
        departamento updated = idepartamento.save(entity);
        return convertToDTO(updated);
    }

    // Eliminar un departamento
    public void deleteDepartamento(int id) {
        if (!idepartamento.existsById(id)) {
            throw new IllegalArgumentException("Departamento no encontrado con id: " + id);
        }
        idepartamento.deleteById(id);
    }

    // Filtrar departamentos por nombre y/o ubicación
    public List<departamentoDTO> filterDepartamentos(String nombre, String ubicacion) {
        List<departamento> lista = idepartamento.findAll();
        return lista.stream().filter(dept -> {
            boolean matchNombre = true;
            boolean matchUbicacion = true;
            if (nombre != null && !nombre.trim().isEmpty()) {
                matchNombre = dept.getNombre().toLowerCase().contains(nombre.toLowerCase());
            }
            if (ubicacion != null && !ubicacion.trim().isEmpty()) {
                matchUbicacion = dept.getUbicacion().toLowerCase().contains(ubicacion.toLowerCase());
            }
            return matchNombre && matchUbicacion;
        }).map(this::convertToDTO).collect(Collectors.toList());
    }
}
