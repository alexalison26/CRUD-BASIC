package com.hospital.pacientes.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.usuario;

@Repository
public interface Iusuario extends JpaRepository<usuario, Integer> {
    // Para iniciar sesión usando correo y contraseña
    Optional<usuario> findByCorreoAndContrasena(String correo, String contrasena);
    
    // Método para filtrar por el nombre
    List<usuario> findByNombreContainingIgnoreCase(String nombre);
}
