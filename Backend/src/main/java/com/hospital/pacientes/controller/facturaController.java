package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.facturaDTO;
import com.hospital.pacientes.service.facturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/factura")
@CrossOrigin(origins = "*")
public class facturaController {

    @Autowired
    private facturaService facturaService;
    
    @GetMapping
    public ResponseEntity<List<facturaDTO>> getAllFacturas() {
        try {
            List<facturaDTO> list = facturaService.getAllFacturas();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getFactura(@PathVariable int id) {
        try {
            facturaDTO dto = facturaService.getFacturaById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createFactura(@RequestBody facturaDTO dto) {
        try {
            facturaDTO nuevo = facturaService.createFactura(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFactura(@PathVariable int id, @RequestBody facturaDTO dto) {
        try {
            facturaDTO actualizado = facturaService.updateFactura(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFactura(@PathVariable int id) {
        try {
            facturaService.deleteFactura(id);
            return new ResponseEntity<>("Factura eliminada correctamente", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
