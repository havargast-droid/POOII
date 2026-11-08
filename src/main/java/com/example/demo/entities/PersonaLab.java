package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Tabla transaccional del laboratorio (personas cargadas desde Excel). */
@Entity
@Table(name = "persona_lab")
@Schema(description = "Persona cargada masivamente desde Excel (Lab 4 complementario)")
public class PersonaLab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pnombre", length = 100)
    private String pnombre;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    public PersonaLab() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPnombre() { return pnombre; }
    public void setPnombre(String pnombre) { this.pnombre = pnombre; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
