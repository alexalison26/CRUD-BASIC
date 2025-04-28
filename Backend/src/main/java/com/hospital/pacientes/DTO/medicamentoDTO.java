package com.hospital.pacientes.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class medicamentoDTO {
    
    private int id;
    private String nombre;
    private String tipo;
    private double costo;
    
    public medicamentoDTO() {
    }
    
    public medicamentoDTO(int id, String nombre, String tipo, double costo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.costo = costo;
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
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public double getCosto() {
        return costo;
    }
    public void setCosto(double costo) {
        this.costo = costo;
    }
}
