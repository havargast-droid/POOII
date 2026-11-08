package com.example.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UsuarioPK implements Serializable {

    @Schema(description = "Serial version u i d")
    private static final long serialVersionUID = 1L;

    @Column(name = "login")
    @Schema(description = "Login")
    private String login;

    @Column(name = "idpersona")
    @Schema(description = "Idpersona")
    private Long idpersona;

    public UsuarioPK() {
    }

    public UsuarioPK(String login, Long idpersona) {
        this.login = login;
        this.idpersona = idpersona;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(Long idpersona) {
        this.idpersona = idpersona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioPK)) return false;
        UsuarioPK that = (UsuarioPK) o;
        return Objects.equals(login, that.login) && Objects.equals(idpersona, that.idpersona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, idpersona);
    }
}
