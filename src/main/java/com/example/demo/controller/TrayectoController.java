package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CrearTrayectoRequest;
import com.example.demo.entities.Trayecto;
import com.example.demo.service.ITrayectoService;

@Tag(name = "Trayecto")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/trayecto")
public class TrayectoController {

    @Autowired
    private ITrayectoService trayectoService;

    @Operation(summary = "Crear")
    @PostMapping
    public ResponseEntity<Trayecto> crear(@RequestBody CrearTrayectoRequest request, Principal principal) {
        String login = (principal != null) ? principal.getName() : "desconocido";
        Trayecto creado = trayectoService.crear(request, login);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    // Servicio 1: consulta de ruta completa (en orden) por codigo de ruta -- protegido
    @Operation(summary = "Servicio 1: consulta de ruta completa (en orden) por codigo de ruta -- protegido")
    @GetMapping("/ruta/{codigoRuta}")
    public ResponseEntity<List<Trayecto>> consultarPorCodigoRuta(@PathVariable String codigoRuta) {
        return ResponseEntity.ok(trayectoService.consultarPorCodigoRuta(codigoRuta));
    }

    // Servicio 2: codigos de ruta agrupados por identificacion de conductor -- protegido
    @Operation(summary = "Servicio 2: codigos de ruta agrupados por identificacion de conductor -- protegido")
    @GetMapping("/conductor/{identificacion}/rutas")
    public ResponseEntity<List<String>> consultarCodigosRutaPorConductor(@PathVariable String identificacion) {
        return ResponseEntity.ok(trayectoService.consultarCodigosRutaPorConductor(identificacion));
    }

    // Servicio 3: codigo de ruta + conductor agrupado, por placa de vehiculo -- protegido
    @Operation(summary = "Servicio 3: codigo de ruta + conductor agrupado, por placa de vehiculo -- protegido")
    @GetMapping("/vehiculo/{placa}/rutas")
    public ResponseEntity<List<Object[]>> consultarRutaYConductorPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(trayectoService.consultarRutaYConductorPorPlaca(placa));
    }

    // Servicio 4: rutas con vehiculo no habilitado o conductor restringido -- protegido
    @Operation(summary = "Servicio 4: rutas con vehiculo no habilitado o conductor restringido -- protegido")
    @GetMapping("/problematicas")
    public ResponseEntity<List<Trayecto>> consultarRutasProblematicas() {
        return ResponseEntity.ok(trayectoService.consultarRutasProblematicas());
    }
}