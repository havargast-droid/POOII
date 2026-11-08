package com.example.demo.service;

import com.example.demo.dto.LicenciaConduccionRequest;
import com.example.demo.entities.Persona;

public interface IPersonaLicenciaService {

    Persona cargarLicencia(Long personaId, LicenciaConduccionRequest request);
}
