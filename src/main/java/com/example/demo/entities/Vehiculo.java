package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "vehiculo")
@Check(constraints = "tipo_vehiculo IN ('AUTOMOVIL','MOTOCICLETA') AND tipo_servicio IN ('Pu','Pr')")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Id")
    private Long id;

    @NotBlank(message = "El tipo de vehiculo es obligatorio")
    @Pattern(regexp = "AUTOMOVIL|MOTOCICLETA", message = "Tipo de vehiculo debe ser AUTOMOVIL o MOTOCICLETA")
    @Column(name = "tipo_vehiculo", nullable = false, length = 12)
    @Schema(description = "Tipo vehiculo")
    private String tipoVehiculo;

    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$|^[A-Z]{3}[0-9]{2}[A-Z]$",
             message = "Formato de placa invalido")
    @Column(name = "placa", nullable = false, unique = true, length = 6)
    @Schema(description = "Placa")
    private String placa;

    @NotBlank(message = "El tipo de servicio es obligatorio")
    @Pattern(regexp = "Pu|Pr", message = "Tipo de servicio debe ser Pu o Pr")
    @Column(name = "tipo_servicio", nullable = false, length = 2)
    @Schema(description = "Tipo servicio")
    private String tipoServicio;

    @NotBlank(message = "El tipo de combustible es obligatorio")
    @Pattern(regexp = "Gasolina|Gas|Diesel", message = "Tipo de combustible invalido")
    @Column(name = "tipo_combustible", nullable = false, length = 10)
    @Schema(description = "Tipo combustible")
    private String tipoCombustible;

    @NotNull(message = "La capacidad de pasajeros es obligatoria")
    @Positive(message = "La capacidad de pasajeros debe ser mayor a 0")
    @Column(name = "capacidad_pasajeros", nullable = false)
    @Schema(description = "Capacidad pasajeros")
    private Integer capacidadPasajeros;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "El color debe estar en formato hexadecimal, ej: #FFFFFF")
    @Column(name = "color", length = 7)
    @Schema(description = "Color")
    private String color;

    @Column(name = "modelo")
    @Schema(description = "Modelo")
    private Integer modelo;

    @Column(name = "marca", length = 50)
    @Schema(description = "Marca")
    private String marca;

    @Column(name = "linea", length = 50)
    @Schema(description = "Linea")
    private String linea;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Documentos")
    private List<VehiculoDocumento> documentos = new ArrayList<>();

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Conductores")
    private List<VehiculoPersona> conductores = new ArrayList<>();

    public Vehiculo() {
    }

    public Vehiculo(String tipoVehiculo, String placa, String tipoServicio, String tipoCombustible,
            Integer capacidadPasajeros, String color, Integer modelo, String marca, String linea) {
        this.tipoVehiculo = tipoVehiculo;
        this.placa = placa;
        this.tipoServicio = tipoServicio;
        this.tipoCombustible = tipoCombustible;
        this.capacidadPasajeros = capacidadPasajeros;
        this.color = color;
        this.modelo = modelo;
        this.marca = marca;
        this.linea = linea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public Integer getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(Integer capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getModelo() {
        return modelo;
    }

    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public List<VehiculoDocumento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<VehiculoDocumento> documentos) {
        this.documentos = documentos;
    }

    public List<VehiculoPersona> getConductores() {
        return conductores;
    }

    public void setConductores(List<VehiculoPersona> conductores) {
        this.conductores = conductores;
    }

    @Override
    public String toString() {
        return "Vehiculo [id=" + id + ", placa=" + placa + ", tipoVehiculo=" + tipoVehiculo + "]";
    }
}
