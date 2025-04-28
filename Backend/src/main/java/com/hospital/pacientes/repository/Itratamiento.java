package com.hospital.pacientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.tratamiento;

@Repository
public interface Itratamiento extends JpaRepository<tratamiento, Integer> {
    // Puedes agregar métodos de búsqueda personalizados si es necesario.
}
