package com.hospital.pacientes.DTO;

public class enfermeroDTO {
    private int id;
    private String nombre;
    private String turno;
    private int aniosExperiencia;

    public enfermeroDTO() {
    }

    public enfermeroDTO(int id, String nombre, String turno, int aniosExperiencia) {
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
