package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.tratamientoDTO;
import com.hospital.pacientes.service.tratamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tratamiento")
@CrossOrigin(origins = "*")
public class tratamientoController {

    @Autowired
    private tratamientoService tratamientoService;
    
    @GetMapping
    public ResponseEntity<List<tratamientoDTO>> getAllTratamientos() {
        try {
            List<tratamientoDTO> list = tratamientoService.getAllTratamientos();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTratamiento(@PathVariable int id) {
        try {
            tratamientoDTO dto = tratamientoService.getTratamientoById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createTratamiento(@RequestBody tratamientoDTO dto) {
        try {
            tratamientoDTO nuevo = tratamientoService.createTratamiento(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTratamiento(@PathVariable int id, @RequestBody tratamientoDTO dto) {
        try {
            tratamientoDTO actualizado = tratamientoService.updateTratamiento(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTratamiento(@PathVariable int id) {
        try {
            tratamientoService.deleteTratamiento(id);
            return new ResponseEntity<>("Tratamiento eliminado correctamente", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
