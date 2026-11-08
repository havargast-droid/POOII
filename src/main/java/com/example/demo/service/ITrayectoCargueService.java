package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CargueResultado;
import com.example.demo.entities.TrayectoTmp;

public interface ITrayectoCargueService {

    /** Lee el Excel y ejecuta cargue -> validacion -> procesado. */
    CargueResultado procesarExcel(MultipartFile file, String login);

    /** Registros de la tabla temporal de un cargue. */
    List<TrayectoTmp> consultarPorIdCargue(Long idCargue);
}
