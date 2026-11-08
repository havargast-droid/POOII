package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Documento;
import com.example.demo.service.IDocumentoService;
import jakarta.validation.Valid;

@Tag(name = "Documento")
@RestController
@RequestMapping("/documento")
public class DocumentoController {

    @Autowired
    private IDocumentoService documentoService;

    @Operation(summary = "Crear")
    @PostMapping
    public ResponseEntity<Documento> crear(@Valid @RequestBody Documento documento) {
        Documento creado = documentoService.crear(documento);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar")
    @GetMapping
    public ResponseEntity<List<Documento>> listar() {
        return ResponseEntity.ok(documentoService.listar());
    }

    @Operation(summary = "Buscar por id")
    @GetMapping("/{id}")
    public ResponseEntity<Documento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.buscarPorId(id));
    }

    @Operation(summary = "Actualizar")
    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizar(@PathVariable Long id, @Valid @RequestBody Documento documento) {
        return ResponseEntity.ok(documentoService.actualizar(id, documento));
    }

    @Operation(summary = "Eliminar")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
