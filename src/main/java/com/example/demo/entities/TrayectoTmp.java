package com.example.demo.entities;

import org.hibernate.annotations.Check;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tabla temporal del cargue masivo (Entrega 4). Guarda las filas del Excel tal cual,
 * mas el estado del proceso, las observaciones de validacion y el id del cargue.
 */
@Entity
@Table(name = "trayecto_tmp")
@Check(constraints = "estado IN ('CARGADO','VALIDADO','PROCESADO','ERROR')")
@Schema(description = "Registro temporal del cargue masivo de trayectos desde Excel")
public class TrayectoTmp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identificacion_conductor")
    private String identificacionConductor;

    @Column(name = "placa")
    private String placa;

    @Column(name = "codigo_ruta")
    private String codigoRuta;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "orden_parada")
    private String ordenParada;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @Column(name = "login_usuario")
    private String loginUsuario;

    @Schema(description = "CARGADO | VALIDADO | PROCESADO | ERROR")
    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @Schema(description = "Errores de validacion acumulados (vacio si no hay errores)")
    @Column(name = "observacion", length = 1000)
    private String observacion;

    @Schema(description = "Identificador del cargue: anio-mes-dia-hora-minuto (yyyyMMddHHmm)")
    @Column(name = "id_cargue", nullable = false)
    private Long idCargue;

    public TrayectoTmp() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdentificacionConductor() { return identificacionConductor; }
    public void setIdentificacionConductor(String v) { this.identificacionConductor = v; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getCodigoRuta() { return codigoRuta; }
    public void setCodigoRuta(String codigoRuta) { this.codigoRuta = codigoRuta; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getOrdenParada() { return ordenParada; }
    public void setOrdenParada(String ordenParada) { this.ordenParada = ordenParada; }
    public String getLatitud() { return latitud; }
    public void setLatitud(String latitud) { this.latitud = latitud; }
    public String getLongitud() { return longitud; }
    public void setLongitud(String longitud) { this.longitud = longitud; }
    public String getLoginUsuario() { return loginUsuario; }
    public void setLoginUsuario(String loginUsuario) { this.loginUsuario = loginUsuario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Long getIdCargue() { return idCargue; }
    public void setIdCargue(Long idCargue) { this.idCargue = idCargue; }
}
