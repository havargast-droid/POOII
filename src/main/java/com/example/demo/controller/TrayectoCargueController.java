package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CargueResultado;
import com.example.demo.entities.TrayectoTmp;
import com.example.demo.service.ITrayectoCargueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/trayecto/cargue")
@Tag(name = "Cargue masivo de trayectos", description = "Entrega 4: carga de trayectos desde Excel con procedimientos almacenados")
public class TrayectoCargueController {

    @Autowired
    private ITrayectoCargueService cargueService;

    @Operation(summary = "Cargar trayectos desde Excel",
            description = "Recibe un .xlsx (form-data, campo 'file') con columnas IDENTIFICACION_CONDUCTOR, PLACA, "
                    + "CODIGO_RUTA, UBICACION, ORDEN_PARADA. Ejecuta cargue, validacion y procesado.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CargueResultado> cargar(@RequestParam("file") MultipartFile file, Principal principal) {
        String login = (principal != null) ? principal.getName() : "desconocido";
        return ResponseEntity.ok(cargueService.procesarExcel(file, login));
    }

    @Operation(summary = "Consultar un cargue por idCargue",
            description = "Devuelve los registros de la tabla temporal con su estado y observaciones")
    @GetMapping("/{idCargue}")
    public ResponseEntity<List<TrayectoTmp>> consultar(@PathVariable Long idCargue) {
        return ResponseEntity.ok(cargueService.consultarPorIdCargue(idCargue));
    }
}
