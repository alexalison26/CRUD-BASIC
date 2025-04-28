package com.hospital.pacientes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.pacientes.model.doctor;

@Repository
public interface Idoctor extends JpaRepository<doctor, Integer> {
    List<doctor> findByNombreContainingIgnoreCase(String nombre);
}
