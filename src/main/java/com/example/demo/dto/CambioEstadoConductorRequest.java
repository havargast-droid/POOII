package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CambioEstadoConductorRequest {

    @Schema(description = "Estado")
    private String estado; // PO, EA, RO

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
