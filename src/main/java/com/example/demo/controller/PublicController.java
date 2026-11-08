package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Persona;
import com.example.demo.entities.Vehiculo;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.PersonaRepository;
import com.example.demo.repository.VehiculoPersonaRepository;
import com.example.demo.repository.VehiculoRepository;

// Servicios publicos: NO requieren token ni APIKey (ver WebSecurityConfig -> permitAll "/public/**")
@Tag(name = "Public")
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VehiculoPersonaRepository vehiculoPersonaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    // Consultar todos los vehiculos que tengan documentos vencidos
    @Operation(summary = "Consultar todos los vehiculos que tengan documentos vencidos")
    @GetMapping("/vehiculos/documentos-vencidos")
    public ResponseEntity<List<Vehiculo>> vehiculosConDocumentosVencidos() {
        return ResponseEntity.ok(vehiculoRepository.findVehiculosConDocumentosVencidos());
    }

    // Consultar todos los conductores que pueden operar
    @Operation(summary = "Consultar todos los conductores que pueden operar")
    @GetMapping("/conductores/pueden-operar")
    public ResponseEntity<List<Persona>> conductoresQuePuedenOperar() {
        return ResponseEntity.ok(vehiculoPersonaRepository.findPersonasQuePuedenOperar());
    }

    // Consultar un vehiculo por placa, con conductores y documentos asociados
    @Operation(summary = "Consultar un vehiculo por placa, con conductores y documentos asociados")
    @GetMapping("/vehiculos/placa/{placa}")
    public ResponseEntity<Vehiculo> vehiculoPorPlaca(@PathVariable String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un vehiculo con placa " + placa));
        return ResponseEntity.ok(vehiculo);
    }

    // Consultar vehiculos con documentos por vencer dentro de N dias
    @Operation(summary = "Consultar vehiculos con documentos por vencer dentro de N dias")
    @GetMapping("/vehiculos/documentos-por-vencer")
    public ResponseEntity<List<Vehiculo>> vehiculosConDocumentosPorVencer(
            @RequestParam(name = "dias") int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return ResponseEntity.ok(vehiculoRepository.findVehiculosConDocumentosPorVencer(fechaLimite));
    }

    // Consultar el total de personas agrupadas por tipo
    @Operation(summary = "Consultar el total de personas agrupadas por tipo")
    @GetMapping("/personas/conteo-por-tipo")
    public ResponseEntity<Map<String, Long>> conteoPersonasPorTipo() {
        Map<String, Long> resultado = new HashMap<>();
        for (Object[] fila : personaRepository.contarPorTipoPersona()) {
            resultado.put((String) fila[0], (Long) fila[1]);
        }
        return ResponseEntity.ok(resultado);
    }
}
