package com.hospital.pacientes.DTO;

import java.time.LocalDateTime;

public class citaDTO {

    private int id;
    private int idPaciente;
    private int idDoctor;
    private LocalDateTime fecha;
    private String descripcion;
    
    // Campos adicionales para mostrar los nombres en el front-end
    private String nombrePaciente;
    private String nombreDoctor;
    
    public citaDTO() {
    }
    
    public citaDTO(int id, int idPaciente, int idDoctor, LocalDateTime fecha, String descripcion) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idDoctor = idDoctor;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
    
    // Getters y setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
    
    public int getIdDoctor() {
        return idDoctor;
    }
    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getNombrePaciente() {
        return nombrePaciente;
    }
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }
    
    public String getNombreDoctor() {
        return nombreDoctor;
    }
    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }
}
