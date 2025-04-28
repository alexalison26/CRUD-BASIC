package com.hospital.pacientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "enfermero")
public class enfermero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enfermero")
    private int id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "turno", length = 50, nullable = false)
    private String turno;

    // Nuevo campo: años de experiencia (sin relación y sin valor de departamento)
    @Column(name = "anios_experiencia", nullable = false)
    private int aniosExperiencia;

    public enfermero() {
    }

    public enfermero(int id, String nombre, String turno, int aniosExperiencia) {
        this.id = id;
        this.nombre = nombre;
        this.turno = turno;
        this.aniosExperiencia = aniosExperiencia;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTurno() {
        return turno;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }
    public int getAniosExperiencia() {
        return aniosExperiencia;
    }
    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }
}
