package com.hospital.pacientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.paciente;
import java.util.List;

@Repository
public interface Ipaciente extends JpaRepository<paciente, Integer> {
    // Método opcional para filtrar por nombre (por ejemplo)
    List<paciente> findByNombreContainingIgnoreCase(String nombre);
}
