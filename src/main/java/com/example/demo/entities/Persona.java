package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "persona")
@Check(constraints = "tipo_identificacion IN ('CC') AND tipo_persona IN ('C','A')")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Id")
    private Long id;

    @NotBlank(message = "La identificacion es obligatoria")
    @Column(name = "identificacion", nullable = false, unique = true, length = 20)
    @Schema(description = "Identificacion")
    private String identificacion;

    @NotBlank(message = "El tipo de identificacion es obligatorio")
    @Pattern(regexp = "CC", message = "Tipo de identificacion debe ser CC")
    @Column(name = "tipo_identificacion", nullable = false, length = 2)
    @Schema(description = "Tipo identificacion")
    private String tipoIdentificacion;

    @NotBlank(message = "Los nombres son obligatorios")
    @Column(name = "nombres", nullable = false, length = 100)
    @Schema(description = "Nombres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(name = "apellidos", nullable = false, length = 100)
    @Schema(description = "Apellidos")
    private String apellidos;

    @Email(message = "El correo no es valido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(name = "email", nullable = false, length = 150)
    @Schema(description = "Email")
    private String email;

    @NotBlank(message = "El tipo de persona es obligatorio")
    @Pattern(regexp = "C|A", message = "Tipo de persona debe ser C (Conductor) o A (Administrativo)")
    @Column(name = "tipo_persona", nullable = false, length = 1)
    @Schema(description = "Tipo persona")
    private String tipoPersona;

    // === Entrega 3: solo aplican cuando tipoPersona = "C" (Conductor) ===

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // no se devuelve en las respuestas (pesado y privado)
    @Lob
    @Column(name = "licencia_conduccion")
    @Schema(description = "Licencia conduccion")
    private byte[] licenciaConduccion;

    @Column(name = "fecha_vigencia_licencia")
    @Schema(description = "Fecha vigencia licencia")
    private LocalDate fechaVigenciaLicencia;

    public Persona() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public byte[] getLicenciaConduccion() {
        return licenciaConduccion;
    }

    public void setLicenciaConduccion(byte[] licenciaConduccion) {
        this.licenciaConduccion = licenciaConduccion;
    }

    public LocalDate getFechaVigenciaLicencia() {
        return fechaVigenciaLicencia;
    }

    public void setFechaVigenciaLicencia(LocalDate fechaVigenciaLicencia) {
        this.fechaVigenciaLicencia = fechaVigenciaLicencia;
    }

    @Override
    public String toString() {
        return "Persona [id=" + id + ", nombres=" + nombres + ", tipoPersona=" + tipoPersona + "]";
    }
}
