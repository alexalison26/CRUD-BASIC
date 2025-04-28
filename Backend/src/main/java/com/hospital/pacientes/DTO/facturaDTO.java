package com.hospital.pacientes.DTO;

public class facturaDTO {
    
    private int id;
    private int idPaciente;
    private String nombrePaciente; // Se rellenará a partir del objeto paciente
    private double total;
    private String fechaPago;
    
    public facturaDTO() {
    }
    
    public facturaDTO(int id, int idPaciente, String nombrePaciente, double total, String fechaPago) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.total = total;
        this.fechaPago = fechaPago;
    }
    
    // Getters y Setters
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
    public String getNombrePaciente() {
        return nombrePaciente;
    }
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public String getFechaPago() {
        return fechaPago;
    }
    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
