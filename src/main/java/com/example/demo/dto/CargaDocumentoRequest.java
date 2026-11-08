package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class CargaDocumentoRequest {

    @Schema(description = "Vehiculo id")
    private Long vehiculoId;
    @Schema(description = "Documento id")
    private Long documentoId;
    @Schema(description = "Archivo base64")
    private String archivoBase64;
    @Schema(description = "Fecha expedicion")
    private LocalDate fechaExpedicion;
    @Schema(description = "Fecha vencimiento")
    private LocalDate fechaVencimiento;

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public Long getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }

    public LocalDate getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(LocalDate fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
