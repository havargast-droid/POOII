package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CambioEstadoDocumentoRequest;
import com.example.demo.dto.CargaDocumentoRequest;
import com.example.demo.entities.VehiculoDocumento;
import com.example.demo.service.IVehiculoDocumentoService;

@Tag(name = "Vehiculo documento upload")
@RestController
@RequestMapping("/vehiculo-documento")
public class VehiculoDocumentoUploadController {

    @Autowired
    private IVehiculoDocumentoService vehiculoDocumentoService;

    // Cargue de uno o varios documentos PDF (en Base64) a la vez
    @Operation(summary = "Cargue de uno o varios documentos PDF (en Base64) a la vez")
    @PostMapping("/cargue")
    public ResponseEntity<List<VehiculoDocumento>> cargarDocumentos(
            @RequestBody List<CargaDocumentoRequest> solicitudes) {
        List<VehiculoDocumento> resultado = vehiculoDocumentoService.cargarDocumentos(solicitudes);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    // Cambiar el estado de un documento de vehiculo (p. ej. En Verificacion -> Habilitado)
    @Operation(summary = "Cambiar el estado de un documento del vehiculo (Habilitado, Vencido, En Verificacion)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<VehiculoDocumento> cambiarEstado(@PathVariable Long id,
            @RequestBody CambioEstadoDocumentoRequest request) {
        return ResponseEntity.ok(vehiculoDocumentoService.cambiarEstado(id, request.getEstado()));
    }
}
