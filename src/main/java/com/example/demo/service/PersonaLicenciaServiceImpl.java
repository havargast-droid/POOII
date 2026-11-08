package com.example.demo.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.LicenciaConduccionRequest;
import com.example.demo.entities.Persona;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.PersonaRepository;

@Service
public class PersonaLicenciaServiceImpl implements IPersonaLicenciaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    @Transactional
    public Persona cargarLicencia(Long personaId, LicenciaConduccionRequest request) {
        Persona persona = personaRepository.findById(personaId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una persona con id " + personaId));

        if (!"C".equals(persona.getTipoPersona())) {
            throw new ReglaDeNegocioException(
                "La licencia de conduccion solo aplica para personas de tipo CONDUCTOR");
        }

        byte[] archivo;
        try {
            archivo = Base64.getDecoder().decode(request.getArchivoBase64());
        } catch (IllegalArgumentException e) {
            throw new ReglaDeNegocioException("El archivo de licencia no es Base64 valido");
        }

        persona.setLicenciaConduccion(archivo);
        persona.setFechaVigenciaLicencia(request.getFechaVigencia());

        return personaRepository.save(persona);
    }
}
