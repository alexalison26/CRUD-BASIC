package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.enfermeroDTO;
import com.hospital.pacientes.service.enfermeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enfermero")
@CrossOrigin(origins = "*")
public class enfermeroController {

    @Autowired
    private enfermeroService enfermeroService;

    @GetMapping
    public ResponseEntity<List<enfermeroDTO>> getAllEnfermeros() {
        try {
            List<enfermeroDTO> list = enfermeroService.getAllEnfermeros();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnfermero(@PathVariable int id) {
        try {
            enfermeroDTO dto = enfermeroService.getEnfermeroById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/filter")
    public ResponseEntity<?> filterEnfermeros(@RequestParam(required = false) String nombre,
                                              @RequestParam(required = false) String turno) {
        try {
            List<enfermeroDTO> list = enfermeroService.filterEnfermeros(nombre, turno);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEnfermero(@RequestBody enfermeroDTO dto) {
        try {
            enfermeroDTO nuevo = enfermeroService.createEnfermero(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnfermero(@PathVariable int id, @RequestBody enfermeroDTO dto) {
        try {
            enfermeroDTO actualizado = enfermeroService.updateEnfermero(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnfermero(@PathVariable int id) {
        try {
            enfermeroService.deleteEnfermero(id);
            return new ResponseEntity<>("Enfermero eliminado correctamente.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
