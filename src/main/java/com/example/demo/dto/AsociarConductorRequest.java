package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AsociarConductorRequest {

    @Schema(description = "Vehiculo id")
    private Long vehiculoId;
    @Schema(description = "Persona id")
    private Long personaId;

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }
}
