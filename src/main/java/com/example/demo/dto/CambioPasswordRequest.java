package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CambioPasswordRequest {

    @Schema(description = "Password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
