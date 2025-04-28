package com.hospital.pacientes.service;

import com.hospital.pacientes.DTO.medicamentoDTO;
import com.hospital.pacientes.model.medicamento;
import com.hospital.pacientes.repository.Imedicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class medicamentoService {

    @Autowired
    private Imedicamento imedicamento;
    
    // Conversión de entidad a DTO
    private medicamentoDTO convertToDTO(medicamento entity) {
        return new medicamentoDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getTipo(),
            entity.getCosto()
        );
    }
    
    // Listar todos los medicamentos
    public List<medicamentoDTO> getAllMedicamentos() {
        List<medicamento> lista = imedicamento.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    // Obtener medicamento por id
    public medicamentoDTO getMedicamentoById(int id) {
        Optional<medicamento> opt = imedicamento.findById(id);
        if(opt.isPresent()){
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Medicamento no encontrado con id: " + id);
        }
    }
    
    // Crear un nuevo medicamento
    public medicamentoDTO createMedicamento(medicamentoDTO dto) {
        if(dto.getNombre() == null || dto.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if(dto.getTipo() == null || dto.getTipo().trim().isEmpty()){
            throw new IllegalArgumentException("El tipo es obligatorio.");
        }
        // Puedes agregar más validaciones (por ejemplo, que el costo no sea negativo)
        if(dto.getCosto() < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }
        // Al crear un nuevo medicamento, ignoramos el id recibido (lo dejamos en cero o sin asignar)
        medicamento entity = new medicamento();
        entity.setNombre(dto.getNombre());
        entity.setTipo(dto.getTipo());
        entity.setCosto(dto.getCosto());
        medicamento created = imedicamento.save(entity);
        return convertToDTO(created);
    }
    
    // Actualizar medicamento existente
    public medicamentoDTO updateMedicamento(int id, medicamentoDTO dto) {
        if(dto.getNombre() == null || dto.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if(dto.getTipo() == null || dto.getTipo().trim().isEmpty()){
            throw new IllegalArgumentException("El tipo es obligatorio.");
        }
        if(dto.getCosto() < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }
        Optional<medicamento> opt = imedicamento.findById(id);
        if(!opt.isPresent()){
            throw new IllegalArgumentException("Medicamento no encontrado con id: " + id);
        }
        medicamento entity = opt.get();
        entity.setNombre(dto.getNombre());
        entity.setTipo(dto.getTipo());
        entity.setCosto(dto.getCosto());
        medicamento updated = imedicamento.save(entity);
        return convertToDTO(updated);
    }
    
    // Eliminar medicamento
    public void deleteMedicamento(int id) {
        if(!imedicamento.existsById(id)){
            throw new IllegalArgumentException("Medicamento no encontrado con id: " + id);
        }
        imedicamento.deleteById(id);
    }
}
