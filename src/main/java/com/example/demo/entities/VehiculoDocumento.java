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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "vehiculo_documento")
@Check(constraints = "estado_documento IN ('Habilitado','Vencido','En Verificacion')")
public class VehiculoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Id")
    private Long id;

    @NotNull(message = "El vehiculo asociado es obligatorio")
    @JsonIgnoreProperties({"documentos", "conductores", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @Schema(description = "Vehiculo")
    private Vehiculo vehiculo;

    @NotNull(message = "El documento asociado es obligatorio")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    @Schema(description = "Documento")
    private Documento documento;

    @Column(name = "fecha_expedicion")
    @Schema(description = "Fecha expedicion")
    private LocalDate fechaExpedicion;

    @Column(name = "fecha_vencimiento")
    @Schema(description = "Fecha vencimiento")
    private LocalDate fechaVencimiento;

    @Pattern(regexp = "Habilitado|Vencido|En Verificacion", message = "Estado de documento invalido")
    @Column(name = "estado_documento", nullable = false, length = 15)
    @Schema(description = "Estado documento")
    private String estadoDocumento = "En Verificacion";

    // Entrega 2: archivo PDF del documento, almacenado como BLOB (bytes del Base64 decodificado)
    @Lob
    @Column(name = "archivo_documento")
    @Schema(description = "Archivo documento")
    private byte[] archivoDocumento;

    public VehiculoDocumento() {
    }

    public VehiculoDocumento(Vehiculo vehiculo, Documento documento, LocalDate fechaExpedicion,
            LocalDate fechaVencimiento, String estadoDocumento) {
        this.vehiculo = vehiculo;
        this.documento = documento;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoDocumento = estadoDocumento;
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

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
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

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(String estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public byte[] getArchivoDocumento() {
        return archivoDocumento;
    }

    public void setArchivoDocumento(byte[] archivoDocumento) {
        this.archivoDocumento = archivoDocumento;
    }

    @Override
    public String toString() {
        return "VehiculoDocumento [id=" + id + ", estado=" + estadoDocumento + "]";
    }
}
