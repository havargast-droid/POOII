package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuario")
public class Usuario {

    @EmbeddedId
    @Schema(description = "Id")
    private UsuarioPK id = new UsuarioPK();

    @ManyToOne
    @MapsId("idpersona")
    @JoinColumn(name = "idpersona", nullable = false)
    @Schema(description = "Persona")
    private Persona persona;

    @NotBlank
    @Column(name = "password", nullable = false, length = 100)
    @Schema(description = "Password")
    private String password;

    @Column(name = "apikey", length = 100)
    @Schema(description = "Apikey")
    private String apikey;

    public Usuario() {
    }

    public Usuario(String login, Persona persona, String password, String apikey) {
        this.id = new UsuarioPK(login, persona.getId());
        this.persona = persona;
        this.password = password;
        this.apikey = apikey;
    }

    public UsuarioPK getId() {
        return id;
    }

    public void setId(UsuarioPK id) {
        this.id = id;
    }

    public String getLogin() {
        return id.getLogin();
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public String toString() {
        return "Usuario [login=" + getLogin() + "]";
    }
}
