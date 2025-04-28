package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.pacienteDTO;
import com.hospital.pacientes.service.pacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/paciente")
@CrossOrigin(origins = "*")
public class pacienteController {

    @Autowired
    private pacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<pacienteDTO>> getAllPacientes() {
        try {
            List<pacienteDTO> lista = pacienteService.getAllPacientes();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaciente(@PathVariable int id) {
        try {
            pacienteDTO dto = pacienteService.getPacienteById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPaciente(@RequestBody pacienteDTO dto) {
        try {
            pacienteDTO nuevo = pacienteService.createPaciente(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaciente(@PathVariable int id, @RequestBody pacienteDTO dto) {
        try {
            pacienteDTO actualizado = pacienteService.updatePaciente(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaciente(@PathVariable int id) {
        try {
            pacienteService.deletePaciente(id);
            return new ResponseEntity<>("Paciente eliminado correctamente. Todas las citas asignadas serán eliminadas.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
