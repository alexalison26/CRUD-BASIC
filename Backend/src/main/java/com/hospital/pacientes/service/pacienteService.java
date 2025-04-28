package com.hospital.pacientes.service;

import com.hospital.pacientes.DTO.pacienteDTO;
import com.hospital.pacientes.model.paciente;
import com.hospital.pacientes.model.cita;
import com.hospital.pacientes.model.factura; // Asegúrate de importar la clase factura
import com.hospital.pacientes.repository.Ipaciente;
import com.hospital.pacientes.repository.Icita;
import com.hospital.pacientes.repository.Ifactura; // Repositorio para factura
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class pacienteService {

    @Autowired
    private Ipaciente ipaciente;
    
    @Autowired
    private Icita icita;

    @Autowired
    private Ifactura ifactura; // Inyectamos el repositorio de facturas

    // Conversión de entidad a DTO
    private pacienteDTO convertToDTO(paciente entity) {
        return new pacienteDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getFechaNacimiento(),
                entity.getDireccion()
        );
    }

    // Conversión de DTO a entidad
    private paciente convertToEntity(pacienteDTO dto) {
        paciente entity = new paciente();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setDireccion(dto.getDireccion());
        return entity;
    }

    // Listar todos los pacientes
    public List<pacienteDTO> getAllPacientes() {
        List<paciente> lista = ipaciente.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener paciente por ID
    public pacienteDTO getPacienteById(int id) {
        Optional<paciente> opt = ipaciente.findById(id);
        if (opt.isPresent()) {
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Paciente no encontrado con id: " + id);
        }
    }

    // Crear un paciente con validaciones
    public pacienteDTO createPaciente(pacienteDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (dto.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
        if (dto.getDireccion() == null || dto.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        paciente entity = convertToEntity(dto);
        paciente created = ipaciente.save(entity);
        return convertToDTO(created);
    }

    // Actualizar paciente existente
    public pacienteDTO updatePaciente(int id, pacienteDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (dto.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
        if (dto.getDireccion() == null || dto.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        Optional<paciente> optional = ipaciente.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Paciente no encontrado con id: " + id);
        }
        paciente entity = optional.get();
        entity.setNombre(dto.getNombre());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setDireccion(dto.getDireccion());
        paciente updated = ipaciente.save(entity);
        return convertToDTO(updated);
    }

    // Eliminar paciente: se eliminan todas sus citas y facturas antes de borrarlo
    public void deletePaciente(int id) {
        if (!ipaciente.existsById(id)) {
            throw new IllegalArgumentException("Paciente no encontrado con id: " + id);
        }
        // Antes de eliminar, se notifica que se procederá a eliminar todas las citas y facturas asociadas.
        System.out.println("Atención: Al eliminar el paciente, se eliminarán todas las citas y facturas asociadas.");

        // Eliminar facturas asociadas
        List<factura> facturas = ifactura.findAll().stream()
                .filter(f -> f.getPaciente().getId() == id)
                .collect(Collectors.toList());
        if (!facturas.isEmpty()) {
            ifactura.deleteAll(facturas);
        }

        // Eliminar citas asociadas
        List<cita> citas = icita.findAll().stream()
                .filter(c -> c.getPaciente().getId() == id)
                .collect(Collectors.toList());
        if (!citas.isEmpty()) {
            icita.deleteAll(citas);
        }
        // Finalmente, eliminar el paciente.
        ipaciente.deleteById(id);
    }
}
