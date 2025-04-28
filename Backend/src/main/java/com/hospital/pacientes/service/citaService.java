package com.hospital.pacientes.service;

import com.hospital.pacientes.model.cita;
import com.hospital.pacientes.DTO.citaDTO;
import com.hospital.pacientes.repository.Icita;
import com.hospital.pacientes.repository.Ipaciente;
import com.hospital.pacientes.repository.Idoctor;
import com.hospital.pacientes.model.paciente;
import com.hospital.pacientes.model.doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class citaService {

    @Autowired
    private Icita icita;
    
    @Autowired
    private Ipaciente ipaciente; // Repositorio de paciente

    @Autowired
    private Idoctor idoctor; // Repositorio de doctor

    // Conversión de entidad a DTO
    private citaDTO convertToDTO(cita entity) {
        citaDTO dto = new citaDTO(
            entity.getId(),
            entity.getPaciente().getId(),
            entity.getDoctor().getId(),
            entity.getFecha(),
            entity.getDescripcion()
        );
        dto.setNombrePaciente(entity.getPaciente().getNombre());
        dto.setNombreDoctor(entity.getDoctor().getNombre());
        return dto;
    }

    // Conversión de DTO a entidad
    private cita convertToEntity(citaDTO dto) {
        cita entity = new cita();
        entity.setId(dto.getId());
        // Recuperar pacientes y doctor por ID
        Optional<paciente> pacienteOpt = ipaciente.findById(dto.getIdPaciente());
        Optional<doctor> doctorOpt = idoctor.findById(dto.getIdDoctor());
        if (!pacienteOpt.isPresent()) {
            throw new IllegalArgumentException("Paciente no encontrado con id: " + dto.getIdPaciente());
        }
        if (!doctorOpt.isPresent()) {
            throw new IllegalArgumentException("Doctor no encontrado con id: " + dto.getIdDoctor());
        }
        entity.setPaciente(pacienteOpt.get());
        entity.setDoctor(doctorOpt.get());
        entity.setFecha(dto.getFecha());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }
    
    // Listar todas las citas
    public List<citaDTO> getAllCitas() {
        List<cita> lista = icita.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    // Obtener cita por ID
    public citaDTO getCitaById(int id) {
        Optional<cita> opt = icita.findById(id);
        if (opt.isPresent()) {
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Cita no encontrada con id: " + id);
        }
    }
    
    // Crear una nueva cita (la fecha no puede estar en el pasado)
    public citaDTO createCita(citaDTO dto) {
        if (dto.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("El ID del paciente es obligatorio y debe ser positivo.");
        }
        if (dto.getIdDoctor() <= 0) {
            throw new IllegalArgumentException("El ID del doctor es obligatorio y debe ser positivo.");
        }
        if (dto.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (dto.getFecha().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la cita no puede estar en el pasado.");
        }
        cita entity = convertToEntity(dto);
        cita created = icita.save(entity);
        return convertToDTO(created);
    }
    
    // Actualizar una cita
    public citaDTO updateCita(int id, citaDTO dto) {
        if (dto.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("El ID del paciente es obligatorio y debe ser positivo.");
        }
        if (dto.getIdDoctor() <= 0) {
            throw new IllegalArgumentException("El ID del doctor es obligatorio y debe ser positivo.");
        }
        if (dto.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (dto.getFecha().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la cita no puede estar en el pasado.");
        }
        Optional<cita> optional = icita.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + id);
        }
        cita entity = optional.get();
        Optional<paciente> pacienteOpt = ipaciente.findById(dto.getIdPaciente());
        Optional<doctor> doctorOpt = idoctor.findById(dto.getIdDoctor());
        if (!pacienteOpt.isPresent()) {
            throw new IllegalArgumentException("Paciente no encontrado con id: " + dto.getIdPaciente());
        }
        if (!doctorOpt.isPresent()) {
            throw new IllegalArgumentException("Doctor no encontrado con id: " + dto.getIdDoctor());
        }
        entity.setPaciente(pacienteOpt.get());
        entity.setDoctor(doctorOpt.get());
        entity.setFecha(dto.getFecha());
        entity.setDescripcion(dto.getDescripcion());
        cita updated = icita.save(entity);
        return convertToDTO(updated);
    }
    
    // Eliminar una cita
    public void deleteCita(int id) {
        if (!icita.existsById(id)) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + id);
        }
        icita.deleteById(id);
    }
    
    // Filtrar citas por descripción (substring, case-insensitive) y por igualdad de IDs de paciente y doctor
    public List<citaDTO> filterCitas(String descripcion, Integer idPaciente, Integer idDoctor) {
        List<cita> lista = icita.findAll();
        return lista.stream().filter(c -> {
            boolean match = true;
            if (descripcion != null && !descripcion.trim().isEmpty()) {
                match = (c.getDescripcion() != null &&
                         c.getDescripcion().toLowerCase().contains(descripcion.toLowerCase()));
            }
            if (idPaciente != null && idPaciente > 0) {
                match = match && (c.getPaciente().getId() == idPaciente);
            }
            if (idDoctor != null && idDoctor > 0) {
                match = match && (c.getDoctor().getId() == idDoctor);
            }
            return match;
        }).map(this::convertToDTO).collect(Collectors.toList());
    }
}
