package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AsociarConductorRequest;
import com.example.demo.dto.CambioEstadoConductorRequest;
import com.example.demo.entities.VehiculoPersona;
import com.example.demo.service.IVehiculoPersonaService;

@Tag(name = "Vehiculo persona")
@RestController
@RequestMapping("/vehiculo-persona")
public class VehiculoPersonaController {

    @Autowired
    private IVehiculoPersonaService vehiculoPersonaService;

    @Operation(summary = "Asociar conductor")
    @PostMapping
    public ResponseEntity<VehiculoPersona> asociarConductor(@RequestBody AsociarConductorRequest request) {
        VehiculoPersona creado = vehiculoPersonaService.asociarConductor(
            request.getVehiculoId(), request.getPersonaId());
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @Operation(summary = "Cambiar estado")
    @PutMapping("/{id}/estado")
    public ResponseEntity<VehiculoPersona> cambiarEstado(
            @PathVariable Long id, @RequestBody CambioEstadoConductorRequest request) {
        return ResponseEntity.ok(vehiculoPersonaService.cambiarEstado(id, request.getEstado()));
    }
}
