package com.hospital.pacientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "habitacion")
public class habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private int id;

    @Column(name = "numero", length = 10, nullable = false)
    private String numero;

    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @Column(name = "capacidad", nullable = false)
    private int capacidad;

    // Constructor sin argumentos
    public habitacion() {
    }

    // Constructor sin ID (para nuevos registros)
    public habitacion(String numero, String descripcion, int capacidad) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
    }
    
    // Constructor completo (opcional)
    public habitacion(int id, String numero, String descripcion, int capacidad) {
        this.id = id;
        this.numero = numero;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
}
