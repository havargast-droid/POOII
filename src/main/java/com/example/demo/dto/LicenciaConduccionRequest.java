package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class LicenciaConduccionRequest {

    @Schema(description = "Archivo base64")
    private String archivoBase64;
    @Schema(description = "Fecha vigencia")
    private LocalDate fechaVigencia;

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }

    public LocalDate getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(LocalDate fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }
}
