package com.hospital.pacientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.enfermero;
import java.util.List;

@Repository
public interface Ienfermero extends JpaRepository<enfermero, Integer> {
    // Método opcional para filtrar por nombre (puedes agregar otros si lo requieres)
    List<enfermero> findByNombreContainingIgnoreCase(String nombre);
}
