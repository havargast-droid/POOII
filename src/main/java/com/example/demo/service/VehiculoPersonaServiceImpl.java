package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Persona;
import com.example.demo.entities.Vehiculo;
import com.example.demo.entities.VehiculoPersona;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.PersonaRepository;
import com.example.demo.repository.VehiculoPersonaRepository;
import com.example.demo.repository.VehiculoRepository;

@Service
public class VehiculoPersonaServiceImpl implements IVehiculoPersonaService {

    @Autowired
    private VehiculoPersonaRepository vehiculoPersonaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    @Transactional
    public VehiculoPersona asociarConductor(Long vehiculoId, Long personaId) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un vehiculo con id " + vehiculoId));

        Persona persona = personaRepository.findById(personaId)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una persona con id " + personaId));

        if (!"C".equals(persona.getTipoPersona())) {
            throw new ReglaDeNegocioException(
                "Solo se pueden asociar personas de tipo CONDUCTOR a un vehiculo");
        }

        VehiculoPersona vp = new VehiculoPersona();
        vp.setVehiculo(vehiculo);
        vp.setPersona(persona);
        // fechaAsociacion y estadoConductor ya tienen valores por defecto en la entidad

        return vehiculoPersonaRepository.save(vp);
    }

    @Override
    @Transactional
    public VehiculoPersona cambiarEstado(Long id, String nuevoEstado) {
        VehiculoPersona vp = vehiculoPersonaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe una relacion vehiculo-conductor con id " + id));

        if (!nuevoEstado.matches("PO|EA|RO")) {
            throw new ReglaDeNegocioException("Estado de conductor invalido. Use PO, EA o RO");
        }

        vp.setEstadoConductor(nuevoEstado);
        return vehiculoPersonaRepository.save(vp);
    }

    @Override
    public List<VehiculoPersona> listarPorVehiculo(Long vehiculoId) {
        return vehiculoPersonaRepository.findByVehiculoId(vehiculoId);
    }

    @Override
    public List<Persona> listarConductoresQuePuedenOperar() {
        return vehiculoPersonaRepository.findPersonasQuePuedenOperar();
    }
}
