package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Persona;
import com.example.demo.service.IPersonaService;
import jakarta.validation.Valid;

@Tag(name = "Persona")
@RestController
@RequestMapping("/persona")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @Operation(summary = "Crear")
    @PostMapping
    public ResponseEntity<Persona> crear(@Valid @RequestBody Persona persona) {
        Persona creada = personaService.crear(persona);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar")
    @GetMapping
    public ResponseEntity<List<Persona>> listar() {
        return ResponseEntity.ok(personaService.listar());
    }

    @Operation(summary = "Buscar por id")
    @GetMapping("/{id}")
    public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.buscarPorId(id));
    }

    @Operation(summary = "Actualizar")
    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @Valid @RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.actualizar(id, persona));
    }

    @Operation(summary = "Conteo por tipo")
    @GetMapping("/conteo-por-tipo")
    public ResponseEntity<Map<String, Long>> conteoPorTipo() {
        Map<String, Long> resultado = new HashMap<>();
        for (Object[] fila : personaService.contarPorTipo()) {
            resultado.put((String) fila[0], (Long) fila[1]);
        }
        return ResponseEntity.ok(resultado);
    }
}
