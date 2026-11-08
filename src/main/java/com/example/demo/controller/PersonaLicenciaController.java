package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LicenciaConduccionRequest;
import com.example.demo.entities.Persona;
import com.example.demo.service.IPersonaLicenciaService;

@Tag(name = "Persona licencia")
@RestController
@RequestMapping("/persona")
public class PersonaLicenciaController {

    @Autowired
    private IPersonaLicenciaService personaLicenciaService;

    @Operation(summary = "Cargar licencia")
    @PostMapping("/{id}/licencia")
    public ResponseEntity<Persona> cargarLicencia(
            @PathVariable Long id, @RequestBody LicenciaConduccionRequest request) {
        return ResponseEntity.ok(personaLicenciaService.cargarLicencia(id, request));
    }
}
