package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CambioEstadoDocumentoRequest {

    @Schema(description = "Nuevo estado: Habilitado, Vencido o En Verificacion")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
