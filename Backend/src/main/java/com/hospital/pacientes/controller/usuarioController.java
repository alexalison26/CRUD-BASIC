package com.hospital.pacientes.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.pacientes.DTO.usuarioDTO;
import com.hospital.pacientes.model.usuario;
import com.hospital.pacientes.repository.Iusuario;
import com.hospital.pacientes.service.usuarioService;

@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin(origins = "*")
public class usuarioController {

    @Autowired
    private usuarioService usuarioService;

    @Autowired
    private Iusuario iusuario; // Usado solo en el método login

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<usuarioDTO>> getAllUsuarios() {
        try {
            List<usuarioDTO> usuarios = usuarioService.getAllUsuarios();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable int id) {
        try {
            usuarioDTO usu = usuarioService.getUsuarioById(id);
            return new ResponseEntity<>(usu, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Filtrar usuarios por nombre
    @GetMapping("/filter")
    public ResponseEntity<?> filterUsuarios(@RequestParam(required = false) String nombre) {
        try {
            List<usuarioDTO> usuarios = usuarioService.filterUsuarios(nombre);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody usuarioDTO usuDTO) {
        try {
            usuarioDTO nuevo = usuarioService.createUsuario(usuDTO);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable int id, @RequestBody usuarioDTO usuDTO) {
        try {
            usuarioDTO actualizado = usuarioService.updateUsuario(id, usuDTO);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int id) {
        try {
            usuarioService.deleteUsuario(id);
            return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login de usuario usando correo y contraseña
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody usuarioDTO usuarioDTO) {
        if (usuarioDTO.getCorreo() == null || usuarioDTO.getContrasena() == null) {
            return new ResponseEntity<>("Correo o contraseña no proporcionados", HttpStatus.BAD_REQUEST);
        }

        Optional<usuario> optionalUser = iusuario.findByCorreoAndContrasena(
            usuarioDTO.getCorreo(), usuarioDTO.getContrasena()
        );

        if (optionalUser.isPresent()) {
            return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }
    }
}
