package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Trayecto;
import com.example.demo.service.ITrayectoService;

// Servicio de apoyo para el mapa HTML (punto "Requerimiento FrontEnd" del enunciado).
// Se deja publico con @CrossOrigin para que la pagina HTML local pueda consumirlo sin token,
// igual que en el ejemplo del Laboratorio 3 del profesor.
@Tag(name = "Mapa")
@RestController
@RequestMapping("/public/mapa")
public class MapaController {

    @Autowired
    private ITrayectoService trayectoService;

    @CrossOrigin(origins = "*")
    @Operation(summary = "Trayectos de ruta")
    @GetMapping("/ruta/{codigoRuta}")
    public ResponseEntity<List<Trayecto>> trayectosDeRuta(@PathVariable String codigoRuta) {
        return ResponseEntity.ok(trayectoService.consultarPorCodigoRuta(codigoRuta));
    }
}
