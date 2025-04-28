package com.hospital.pacientes.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class habitacionDTO {
    private int id;
    private String numero;
    private String descripcion;
    private int capacidad;
    
    public habitacionDTO() {
    }
    
    public habitacionDTO(int id, String numero, String descripcion, int capacidad) {
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
