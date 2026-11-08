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

import com.example.demo.entities.Vehiculo;
import com.example.demo.entities.VehiculoDocumento;
import com.example.demo.service.IVehiculoService;
import jakarta.validation.Valid;

@Tag(name = "Vehiculo")
@RestController
@RequestMapping("/vehiculo")
public class VehiculoController {

    @Autowired
    private IVehiculoService vehiculoService;

    // ============== CRUD ==============

    @Operation(summary = "Crear")
    @PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody Vehiculo vehiculo) {
        Vehiculo creado = vehiculoService.crear(vehiculo);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar")
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        return ResponseEntity.ok(vehiculoService.listar());
    }

    @Operation(summary = "Buscar por id")
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.buscarPorId(id));
    }

    @Operation(summary = "Actualizar")
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @Valid @RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, vehiculo));
    }

    @Operation(summary = "Eliminar")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============== BUSQUEDAS ESPECIFICAS ==============

    @Operation(summary = "Buscar por placa")
    @GetMapping("/placa/{placa}")
    public ResponseEntity<Vehiculo> buscarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(vehiculoService.buscarPorPlaca(placa));
    }

    @Operation(summary = "Buscar por tipo")
    @GetMapping("/tipo/{tipoVehiculo}")
    public ResponseEntity<List<Vehiculo>> buscarPorTipo(@PathVariable String tipoVehiculo) {
        return ResponseEntity.ok(vehiculoService.buscarPorTipoVehiculo(tipoVehiculo));
    }

    @Operation(summary = "Buscar por documento comun")
    @GetMapping("/documento/{codigoDocumento}")
    public ResponseEntity<List<Vehiculo>> buscarPorDocumentoComun(@PathVariable String codigoDocumento) {
        return ResponseEntity.ok(vehiculoService.buscarPorDocumentoComun(codigoDocumento));
    }

    @Operation(summary = "Buscar por estado documento")
    @GetMapping("/estado-documento/{estado}")
    public ResponseEntity<List<Vehiculo>> buscarPorEstadoDocumento(@PathVariable String estado) {
        return ResponseEntity.ok(vehiculoService.buscarPorEstadoDocumento(estado));
    }

    // ============== AGREGAR DOCUMENTO A VEHICULO EXISTENTE ==============

    @Operation(summary = "Agregar documento")
    @PostMapping("/{id}/documento")
    public ResponseEntity<Vehiculo> agregarDocumento(@PathVariable Long id,
            @RequestBody VehiculoDocumento nuevoDocumento) {
        Vehiculo actualizado = vehiculoService.agregarDocumento(id, nuevoDocumento);
        return new ResponseEntity<>(actualizado, HttpStatus.CREATED);
    }
}
