package com.example.demo.controller;

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
import com.example.demo.entities.PersonaLab;
import com.example.demo.entities.PersonaTmp;
import com.example.demo.service.IPersonaCargueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/persona-cargue")
@Tag(name = "Cargue masivo de personas", description = "Laboratorio 4 complementario: Excel de personas con procedimientos almacenados")
public class PersonaCargueController {

    @Autowired
    private IPersonaCargueService cargueService;

    @Operation(summary = "Cargar personas desde Excel (columnas NOMBRE, EDAD, UBICACION)")
    @PostMapping(value = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CargueResultado> cargar(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(cargueService.procesarExcel(file));
    }

    @Operation(summary = "Consultar un cargue de personas por idCargue")
    @GetMapping("/{idCargue}")
    public ResponseEntity<List<PersonaTmp>> consultar(@PathVariable Long idCargue) {
        return ResponseEntity.ok(cargueService.consultarPorIdCargue(idCargue));
    }

    @Operation(summary = "Listar las personas ya procesadas (tabla persona_lab)")
    @GetMapping("/procesadas")
    public ResponseEntity<List<PersonaLab>> procesadas() {
        return ResponseEntity.ok(cargueService.listarPersonasCargadas());
    }
}
