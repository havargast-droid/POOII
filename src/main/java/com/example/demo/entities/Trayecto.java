package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "trayecto")
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Id")
    private Long id;

    @NotNull(message = "La persona (conductor) es obligatoria")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpersona", nullable = false)
    @Schema(description = "Persona")
    private Persona persona;

    @NotNull(message = "El vehiculo es obligatorio")
    @JsonIgnoreProperties({"documentos", "conductores", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idvehiculo", nullable = false)
    @Schema(description = "Vehiculo")
    private Vehiculo vehiculo;

    @NotBlank(message = "El codigo de ruta es obligatorio")
    @Column(name = "codigo_ruta", nullable = false, length = 30)
    @Schema(description = "Codigo ruta")
    private String codigoRuta;

    @NotBlank(message = "La ubicacion es obligatoria")
    @Column(name = "ubicacion", nullable = false, length = 255)
    @Schema(description = "Ubicacion")
    private String ubicacion;

    @NotNull(message = "El orden de la parada es obligatorio")
    @PositiveOrZero(message = "El orden de parada debe ser 0 o mayor")
    @Column(name = "orden_parada", nullable = false)
    @Schema(description = "Orden parada")
    private Integer ordenParada;

    @Column(name = "latitud")
    @Schema(description = "Latitud")
    private Double latitud;

    @Column(name = "longitud")
    @Schema(description = "Longitud")
    private Double longitud;

    @Column(name = "login_usuario", length = 100)
    @Schema(description = "Login usuario")
    private String loginUsuario;

    public Trayecto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getCodigoRuta() {
        return codigoRuta;
    }

    public void setCodigoRuta(String codigoRuta) {
        this.codigoRuta = codigoRuta;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getOrdenParada() {
        return ordenParada;
    }

    public void setOrdenParada(Integer ordenParada) {
        this.ordenParada = ordenParada;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    @Override
    public String toString() {
        return "Trayecto [id=" + id + ", codigoRuta=" + codigoRuta + ", orden=" + ordenParada + "]";
    }
}
