package com.example.demo.entities;

import org.hibernate.annotations.Check;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "persona_tmp")
@Check(constraints = "estado IN ('CARGADO','VALIDADO','PROCESADO','ERROR')")
@Schema(description = "Registro temporal del cargue masivo de personas (Lab 4 complementario)")
public class PersonaTmp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pnombre", length = 100)
    private String pnombre;

    @Column(name = "edad", length = 3)
    private String edad;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Schema(description = "CARGADO | VALIDADO | PROCESADO | ERROR")
    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @Column(name = "observacion", length = 500)
    private String observacion;

    @Schema(description = "Id del cargue (yyyyMMddHHmm)")
    @Column(name = "id_cargue", nullable = false)
    private Long idCargue;

    public PersonaTmp() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPnombre() { return pnombre; }
    public void setPnombre(String pnombre) { this.pnombre = pnombre; }
    public String getEdad() { return edad; }
    public void setEdad(String edad) { this.edad = edad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Long getIdCargue() { return idCargue; }
    public void setIdCargue(Long idCargue) { this.idCargue = idCargue; }
}
