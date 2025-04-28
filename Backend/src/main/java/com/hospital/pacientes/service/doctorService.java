package com.hospital.pacientes.service;

import com.hospital.pacientes.model.doctor;
import com.hospital.pacientes.model.cita;
import com.hospital.pacientes.DTO.doctorDTO;
import com.hospital.pacientes.repository.Idoctor;
import com.hospital.pacientes.repository.Icita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class doctorService {

    @Autowired
    private Idoctor idoctor;

    // Inyectamos también el repositorio de citas para eliminarlas manualmente
    @Autowired
    private Icita icita;

    // Conversión de entidad a DTO
    private doctorDTO convertToDTO(doctor entity) {
        return new doctorDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getEspecialidad(),
            entity.getTelefono()
        );
    }

    // Conversión de DTO a entidad
    private doctor convertToEntity(doctorDTO dto) {
        doctor entity = new doctor();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setEspecialidad(dto.getEspecialidad());
        entity.setTelefono(dto.getTelefono());
        return entity;
    }

    // Listar todos los doctores
    public List<doctorDTO> getAllDoctores() {
        List<doctor> lista = idoctor.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener doctor por ID
    public doctorDTO getDoctorById(int id) {
        Optional<doctor> opt = idoctor.findById(id);
        if (opt.isPresent()) {
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Doctor no encontrado con id: " + id);
        }
    }

    // Crear un nuevo doctor
    public doctorDTO createDoctor(doctorDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getEspecialidad() == null || dto.getEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        if (dto.getTelefono() == null || dto.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        doctor entity = convertToEntity(dto);
        doctor created = idoctor.save(entity);
        return convertToDTO(created);
    }

    // Actualizar un doctor existente 
    public doctorDTO updateDoctor(int id, doctorDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getEspecialidad() == null || dto.getEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        if (dto.getTelefono() == null || dto.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        Optional<doctor> optional = idoctor.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Doctor no encontrado con id: " + id);
        }
        doctor entity = optional.get();
        entity.setNombre(dto.getNombre());
        entity.setEspecialidad(dto.getEspecialidad());
        entity.setTelefono(dto.getTelefono());
        doctor updated = idoctor.save(entity);
        return convertToDTO(updated);
    }

    // Eliminar un doctor: primero se eliminan manualmente todas las citas asignadas a este doctor
    public void deleteDoctor(int id) {
        if (!idoctor.existsById(id)) {
            throw new IllegalArgumentException("Doctor no encontrado con id: " + id);
        }
        // Buscar y eliminar las citas que tengan este doctor:
        List<cita> citas = icita.findAll().stream()
                .filter(c -> c.getDoctor().getId() == id)
                .collect(Collectors.toList());
        if (!citas.isEmpty()) {
            icita.deleteAll(citas);
        }
        // Ahora se elimina el doctor
        idoctor.deleteById(id);
    }
    
    // Filtrar doctores por nombre, especialidad y teléfono (coincidencia parcial, case-insensitive)
    public List<doctorDTO> filterDoctores(String nombre, String especialidad, String telefono) {
        List<doctor> lista = idoctor.findAll();
        return lista.stream().filter(d -> {
            boolean matchNombre = true;
            boolean matchEspecialidad = true;
            boolean matchTelefono = true;
            if (nombre != null && !nombre.trim().isEmpty()) {
                matchNombre = d.getNombre().toLowerCase().contains(nombre.toLowerCase());
            }
            if (especialidad != null && !especialidad.trim().isEmpty()) {
                matchEspecialidad = d.getEspecialidad().toLowerCase().contains(especialidad.toLowerCase());
            }
            if (telefono != null && !telefono.trim().isEmpty()) {
                matchTelefono = d.getTelefono().toLowerCase().contains(telefono.toLowerCase());
            }
            return matchNombre && matchEspecialidad && matchTelefono;
        }).map(this::convertToDTO).collect(Collectors.toList());
    }
}
