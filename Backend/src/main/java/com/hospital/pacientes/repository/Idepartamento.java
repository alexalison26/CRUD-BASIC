package com.hospital.pacientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.departamento;
import java.util.List;

@Repository
public interface Idepartamento extends JpaRepository<departamento, Integer> {
    // Método opcional para filtrar por nombre
    List<departamento> findByNombreContainingIgnoreCase(String nombre);
    
    // Puede agregarse también un método similar para ubicacion,
    // pero aquí lo implementaremos en el service.
}
