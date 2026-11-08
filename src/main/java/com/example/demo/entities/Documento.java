package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Id")
    private Long id;

    @NotBlank(message = "El codigo de documento es obligatorio")
    @Column(name = "codigo_documento", nullable = false, unique = true, length = 20)
    @Schema(description = "Codigo documento")
    private String codigoDocumento;

    @NotBlank(message = "El nombre del documento es obligatorio")
    @Column(name = "nombre_documento", nullable = false, length = 100)
    @Schema(description = "Nombre documento")
    private String nombreDocumento;

    @NotBlank(message = "El tipo de vehiculo al que aplica es obligatorio")
    @Pattern(regexp = "A|M|AM", message = "Tipo de vehiculo aplicable debe ser A, M o AM")
    @Column(name = "tipo_vehiculo_aplica", nullable = false, length = 2)
    @Schema(description = "Tipo vehiculo aplica")
    private String tipoVehiculoAplica;

    @NotBlank(message = "La obligatoriedad es obligatoria")
    @Pattern(regexp = "RA|RM|RR", message = "Obligatoriedad debe ser RA, RM o RR")
    @Column(name = "obligatoriedad", nullable = false, length = 2)
    @Schema(description = "Obligatoriedad")
    private String obligatoriedad;

    @Column(name = "descripcion", length = 255)
    @Schema(description = "Descripcion")
    private String descripcion;

    public Documento() {
    }

    public Documento(String codigoDocumento, String nombreDocumento, String tipoVehiculoAplica,
            String obligatoriedad, String descripcion) {
        this.codigoDocumento = codigoDocumento;
        this.nombreDocumento = nombreDocumento;
        this.tipoVehiculoAplica = tipoVehiculoAplica;
        this.obligatoriedad = obligatoriedad;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getTipoVehiculoAplica() {
        return tipoVehiculoAplica;
    }

    public void setTipoVehiculoAplica(String tipoVehiculoAplica) {
        this.tipoVehiculoAplica = tipoVehiculoAplica;
    }

    public String getObligatoriedad() {
        return obligatoriedad;
    }

    public void setObligatoriedad(String obligatoriedad) {
        this.obligatoriedad = obligatoriedad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Documento [id=" + id + ", codigo=" + codigoDocumento + ", nombre=" + nombreDocumento + "]";
    }
}
