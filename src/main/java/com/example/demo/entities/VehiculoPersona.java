package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "vehiculo_persona")
@Check(constraints = "estado_conductor IN ('PO','EA','RO')")
public class VehiculoPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Id")
    private Long id;

    @NotNull
    @JsonIgnoreProperties({"documentos", "conductores", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @Schema(description = "Vehiculo")
    private Vehiculo vehiculo;

    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    @Schema(description = "Persona")
    private Persona persona;

    @Column(name = "fecha_asociacion", nullable = false)
    @Schema(description = "Fecha asociacion")
    private LocalDate fechaAsociacion = LocalDate.now();

    @Column(name = "estado_conductor", nullable = false, length = 2)
    @Schema(description = "Estado conductor")
    private String estadoConductor = "EA"; // PO, EA, RO

    public VehiculoPersona() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LocalDate getFechaAsociacion() {
        return fechaAsociacion;
    }

    public void setFechaAsociacion(LocalDate fechaAsociacion) {
        this.fechaAsociacion = fechaAsociacion;
    }

    public String getEstadoConductor() {
        return estadoConductor;
    }

    public void setEstadoConductor(String estadoConductor) {
        this.estadoConductor = estadoConductor;
    }
}
