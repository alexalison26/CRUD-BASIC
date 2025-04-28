package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.departamentoDTO;
import com.hospital.pacientes.service.departamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departamento")
@CrossOrigin(origins = "*")
public class departamentoController {

    @Autowired
    private departamentoService departamentoService;

    @GetMapping
    public ResponseEntity<List<departamentoDTO>> getAllDepartamentos() {
        try {
            List<departamentoDTO> lista = departamentoService.getAllDepartamentos();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartamento(@PathVariable int id) {
        try {
            departamentoDTO dto = departamentoService.getDepartamentoById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterDepartamentos(@RequestParam(required = false) String nombre,
                                                 @RequestParam(required = false) String ubicacion) {
        try {
            List<departamentoDTO> lista = departamentoService.filterDepartamentos(nombre, ubicacion);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDepartamento(@RequestBody departamentoDTO dto) {
        try {
            departamentoDTO nuevo = departamentoService.createDepartamento(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartamento(@PathVariable int id, @RequestBody departamentoDTO dto) {
        try {
            departamentoDTO actualizado = departamentoService.updateDepartamento(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartamento(@PathVariable int id) {
        try {
            departamentoService.deleteDepartamento(id);
            return new ResponseEntity<>("Departamento eliminado correctamente.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
