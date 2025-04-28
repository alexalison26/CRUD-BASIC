package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.medicamentoDTO;
import com.hospital.pacientes.service.medicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicamento")
@CrossOrigin(origins = "*")
public class medicamentoController {

    @Autowired
    private medicamentoService medicamentoService;
    
    // Listar todos los medicamentos
    @GetMapping
    public ResponseEntity<List<medicamentoDTO>> getAllMedicamentos() {
        try {
            List<medicamentoDTO> list = medicamentoService.getAllMedicamentos();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtener medicamento por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicamento(@PathVariable int id) {
        try {
            medicamentoDTO dto = medicamentoService.getMedicamentoById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Crear un medicamento
    @PostMapping
    public ResponseEntity<?> createMedicamento(@RequestBody medicamentoDTO dto) {
        try {
            medicamentoDTO nuevo = medicamentoService.createMedicamento(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Actualizar medicamento
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicamento(@PathVariable int id, @RequestBody medicamentoDTO dto) {
        try {
            medicamentoDTO actualizado = medicamentoService.updateMedicamento(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Eliminar medicamento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicamento(@PathVariable int id) {
        try {
            medicamentoService.deleteMedicamento(id);
            return new ResponseEntity<>("Medicamento eliminado correctamente", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
