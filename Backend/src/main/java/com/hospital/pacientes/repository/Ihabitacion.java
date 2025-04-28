package com.hospital.pacientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.habitacion;

@Repository
public interface Ihabitacion extends JpaRepository<habitacion, Integer> {
    // Puedes definir métodos personalizados si es necesario.
}
