package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.citaDTO;
import com.hospital.pacientes.service.citaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cita")
@CrossOrigin(origins = "*")
public class citaController {

    @Autowired
    private citaService citaService;
    
    @GetMapping
    public ResponseEntity<List<citaDTO>> getAllCitas() {
        try {
            List<citaDTO> lista = citaService.getAllCitas();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCita(@PathVariable int id) {
        try {
            citaDTO dto = citaService.getCitaById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/filter")
    public ResponseEntity<?> filterCitas(@RequestParam(required = false) String descripcion,
                                         @RequestParam(required = false) Integer idPaciente,
                                         @RequestParam(required = false) Integer idDoctor) {
        try {
            List<citaDTO> lista = citaService.filterCitas(descripcion, idPaciente, idDoctor);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createCita(@RequestBody citaDTO dto) {
        try {
            citaDTO nuevo = citaService.createCita(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCita(@PathVariable int id, @RequestBody citaDTO dto) {
        try {
            citaDTO actualizado = citaService.updateCita(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCita(@PathVariable int id) {
        try {
            citaService.deleteCita(id);
            return new ResponseEntity<>("Cita eliminada correctamente", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
