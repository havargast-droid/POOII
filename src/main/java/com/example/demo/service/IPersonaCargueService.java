package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CargueResultado;
import com.example.demo.entities.PersonaLab;
import com.example.demo.entities.PersonaTmp;

public interface IPersonaCargueService {

    CargueResultado procesarExcel(MultipartFile file);

    List<PersonaTmp> consultarPorIdCargue(Long idCargue);

    List<PersonaLab> listarPersonasCargadas();
}
