package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CrearTrayectoRequest;
import com.example.demo.entities.Persona;
import com.example.demo.entities.Trayecto;
import com.example.demo.entities.Vehiculo;
import com.example.demo.entities.VehiculoDocumento;
import com.example.demo.entities.VehiculoPersona;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.PersonaRepository;
import com.example.demo.repository.TrayectoRepository;
import com.example.demo.repository.VehiculoDocumentoRepository;
import com.example.demo.repository.VehiculoPersonaRepository;
import com.example.demo.repository.VehiculoRepository;

@Service
public class TrayectoServiceImpl implements ITrayectoService {

    // Maximo de paradas intermedias permitidas (sin contar inicial ni final)
    private static final int MAX_PARADAS_INTERMEDIAS = 5;

    @Autowired
    private TrayectoRepository trayectoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VehiculoPersonaRepository vehiculoPersonaRepository;

    @Autowired
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;

    @Override
    @Transactional
    public Trayecto crear(CrearTrayectoRequest request, String loginUsuario) {

        Persona persona = personaRepository.findById(request.getPersonaId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe una persona con id " + request.getPersonaId()));

        if (!"C".equals(persona.getTipoPersona())) {
            throw new ReglaDeNegocioException("Solo se pueden asociar personas de tipo CONDUCTOR a un trayecto");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un vehiculo con id " + request.getVehiculoId()));

        // Regla: los documentos del vehiculo deben estar todos en estado Habilitado
        List<VehiculoDocumento> documentos = vehiculoDocumentoRepository.findByVehiculoId(vehiculo.getId());
        boolean hayDocumentoNoHabilitado = documentos.stream()
            .anyMatch(vd -> !"Habilitado".equals(vd.getEstadoDocumento()));
        if (documentos.isEmpty() || hayDocumentoNoHabilitado) {
            throw new ReglaDeNegocioException(
                "No se puede crear el trayecto: el vehiculo tiene documentos sin estado Habilitado");
        }

        // Regla: el conductor debe poder operar (PO) en relacion a ese vehiculo
        List<VehiculoPersona> relaciones = vehiculoPersonaRepository.findByVehiculoId(vehiculo.getId());
        boolean conductorPuedeOperar = relaciones.stream()
            .anyMatch(vp -> vp.getPersona().getId().equals(persona.getId()) && "PO".equals(vp.getEstadoConductor()));
        if (!conductorPuedeOperar) {
            throw new ReglaDeNegocioException(
                "No se puede crear el trayecto: el conductor no esta en estado PO (Puede Operar) para este vehiculo");
        }

        // Regla: maximo 5 paradas intermedias (6 trayectos: 1 inicial + 5 intermedias + 1 final = max 7 filas,
        // pero validamos que no se exceda el limite de intermedias antes de guardar una nueva que no sea la final)
        long paradasActuales = trayectoRepository.countByCodigoRuta(request.getCodigoRuta());
        if (paradasActuales > MAX_PARADAS_INTERMEDIAS + 1) { // +1 por la parada inicial ya registrada
            throw new ReglaDeNegocioException(
                "El codigo de ruta " + request.getCodigoRuta()
                + " ya alcanzo el maximo de 5 paradas intermedias (mas inicial y final)");
        }

        Trayecto trayecto = new Trayecto();
        trayecto.setPersona(persona);
        trayecto.setVehiculo(vehiculo);
        trayecto.setCodigoRuta(request.getCodigoRuta());
        trayecto.setUbicacion(request.getUbicacion());
        trayecto.setOrdenParada(request.getOrdenParada());
        trayecto.setLoginUsuario(loginUsuario);
        // latitud/longitud se completan despues, de forma automatica, por la tarea programada

        return trayectoRepository.save(trayecto);
    }

    @Override
    public List<Trayecto> consultarPorCodigoRuta(String codigoRuta) {
        return trayectoRepository.findByCodigoRutaOrderByOrdenParadaAsc(codigoRuta);
    }

    @Override
    public List<String> consultarCodigosRutaPorConductor(String identificacion) {
        return trayectoRepository.findCodigosRutaPorIdentificacionConductor(identificacion);
    }

    @Override
    public List<Object[]> consultarRutaYConductorPorPlaca(String placa) {
        return trayectoRepository.findRutaYConductorPorPlaca(placa);
    }

    @Override
    public List<Trayecto> consultarRutasProblematicas() {
        return trayectoRepository.findRutasConVehiculoNoHabilitadoOConductorRestringido();
    }
}
