package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CrearTrayectoRequest {

    @Schema(description = "Persona id")
    private Long personaId;
    @Schema(description = "Vehiculo id")
    private Long vehiculoId;
    @Schema(description = "Codigo ruta")
    private String codigoRuta;
    @Schema(description = "Ubicacion")
    private String ubicacion;
    @Schema(description = "Orden parada")
    private Integer ordenParada;

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
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
}
