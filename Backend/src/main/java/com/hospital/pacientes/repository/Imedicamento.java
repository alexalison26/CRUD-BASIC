package com.hospital.pacientes.repository;

import com.hospital.pacientes.model.enfermero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.medicamento;

import java.util.List;

@Repository
public interface Imedicamento extends JpaRepository<medicamento, Integer> {
    // Aquí puedes definir métodos de búsqueda personalizados si fuese necesario
}
