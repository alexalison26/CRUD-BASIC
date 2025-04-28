package com.hospital.pacientes.controller;

import com.hospital.pacientes.DTO.doctorDTO;
import com.hospital.pacientes.service.doctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@CrossOrigin(origins = "*")
public class doctorController {

    @Autowired
    private doctorService doctorService;

    @GetMapping
    public ResponseEntity<List<doctorDTO>> getAllDoctores() {
        try {
            List<doctorDTO> lista = doctorService.getAllDoctores();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable int id) {
        try {
            doctorDTO dto = doctorService.getDoctorById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/filter")
    public ResponseEntity<?> filterDoctores(@RequestParam(required = false) String nombre,
                                            @RequestParam(required = false) String especialidad,
                                            @RequestParam(required = false) String telefono) {
        try {
            List<doctorDTO> lista = doctorService.filterDoctores(nombre, especialidad, telefono);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody doctorDTO dto) {
        try {
            doctorDTO nuevo = doctorService.createDoctor(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable int id, @RequestBody doctorDTO dto) {
        try {
            doctorDTO actualizado = doctorService.updateDoctor(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable int id) {
        try {
            // Mensaje de confirmación extendido (en el front-end se usará también)
            doctorService.deleteDoctor(id);
            return new ResponseEntity<>("Doctor eliminado correctamente. Todas las citas asociadas serán eliminadas.", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
