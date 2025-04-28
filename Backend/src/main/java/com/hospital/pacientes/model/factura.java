package com.hospital.pacientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "factura")
public class factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private int id;

    // Relación many-to-one hacia la entidad paciente
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private paciente paciente;

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "fecha_pago", nullable = false)
    private String fechaPago;

    public factura() {
    }

    public factura(int id, paciente paciente, double total, String fechaPago) {
        this.id = id;
        this.paciente = paciente;
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
    public paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(paciente paciente) {
        this.paciente = paciente;
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
