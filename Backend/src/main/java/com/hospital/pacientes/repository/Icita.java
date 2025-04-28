package com.hospital.pacientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.cita;
import java.util.List;

@Repository
public interface Icita extends JpaRepository<cita, Integer> {
    List<cita> findByDescripcionContainingIgnoreCase(String descripcion);
}
