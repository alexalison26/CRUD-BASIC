package com.hospital.pacientes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "cita")
public class cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private int id;

    // Relación con paciente; en este ejemplo también configuramos:
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private paciente paciente;

    // Relación con doctor (no se modifica en este caso)
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_doctor", referencedColumnName = "id_doctor")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private doctor doctor;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "descripcion")
    private String descripcion;

    public cita() {
    }

    public cita(int id, paciente paciente, doctor doctor, LocalDateTime fecha, String descripcion) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(paciente paciente) {
        this.paciente = paciente;
    }
    public doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(doctor doctor) {
        this.doctor = doctor;
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
}
