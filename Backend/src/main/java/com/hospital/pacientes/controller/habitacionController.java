package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.habitacionDTO;
import com.hospital.pacientes.service.habitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habitacion")
@CrossOrigin(origins = "*")
public class habitacionController {

    @Autowired
    private habitacionService habitacionService;
    
    @GetMapping
    public ResponseEntity<List<habitacionDTO>> getAllHabitaciones() {
        try {
            List<habitacionDTO> list = habitacionService.getAllHabitaciones();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getHabitacion(@PathVariable int id) {
        try {
            habitacionDTO dto = habitacionService.getHabitacionById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createHabitacion(@RequestBody habitacionDTO dto) {
        try {
            habitacionDTO nuevo = habitacionService.createHabitacion(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabitacion(@PathVariable int id, @RequestBody habitacionDTO dto) {
        try {
            habitacionDTO actualizado = habitacionService.updateHabitacion(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHabitacion(@PathVariable int id) {
        try {
            habitacionService.deleteHabitacion(id);
            return new ResponseEntity<>("Habitación eliminada correctamente", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
