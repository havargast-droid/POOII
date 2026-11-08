package com.example.demo.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Persona;
import com.example.demo.entities.Usuario;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.PersonaRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class PersonaServiceImpl implements IPersonaService {

    private static final String CARACTERES_PASSWORD = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Persona crear(Persona persona) {
        if (personaRepository.existsByIdentificacion(persona.getIdentificacion())) {
            throw new ReglaDeNegocioException(
                "Ya existe una persona con la identificacion " + persona.getIdentificacion());
        }

        Persona creada = personaRepository.save(persona);

        // Regla: solo personas tipo ADMINISTRATIVO ("A") tienen usuario,
        // y se genera de forma automatica al crear la persona.
        if ("A".equals(creada.getTipoPersona())) {
            crearUsuarioParaPersona(creada);
        }

        return creada;
    }

    @Override
    @Transactional
    public Persona actualizar(Long id, Persona persona) {
        Persona existente = buscarPorId(id);

        existente.setIdentificacion(persona.getIdentificacion());
        existente.setTipoIdentificacion(persona.getTipoIdentificacion());
        existente.setNombres(persona.getNombres());
        existente.setApellidos(persona.getApellidos());
        existente.setEmail(persona.getEmail());

        boolean eraAdministrativo = "A".equals(existente.getTipoPersona());
        existente.setTipoPersona(persona.getTipoPersona());

        Persona actualizada = personaRepository.save(existente);

        // Si cambio a ADMINISTRATIVO y todavia no tenia usuario, se crea ahora
        if (!eraAdministrativo && "A".equals(actualizada.getTipoPersona())
                && usuarioRepository.findByPersonaId(actualizada.getId()).isEmpty()) {
            crearUsuarioParaPersona(actualizada);
        }

        return actualizada;
    }

    @Override
    public List<Persona> listar() {
        return personaRepository.findAll();
    }

    @Override
    public Persona buscarPorId(Long id) {
        return personaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una persona con id " + id));
    }

    @Override
    public List<Object[]> contarPorTipo() {
        return personaRepository.contarPorTipoPersona();
    }

    private void crearUsuarioParaPersona(Persona persona) {
        String login = generarLogin(persona);
        String password = generarPassword();
        String apikey = generarApiKey();

        Usuario usuario = new Usuario(login, persona, password, apikey);
        usuarioRepository.save(usuario);
    }

    // Regla de nemotecnia: primera letra del nombre + primera letra del apellido + identificacion
    private String generarLogin(Persona persona) {
        String inicialNombre = persona.getNombres().trim().substring(0, 1).toLowerCase();
        String inicialApellido = persona.getApellidos().trim().substring(0, 1).toLowerCase();
        return inicialNombre + inicialApellido + persona.getIdentificacion();
    }

    private String generarPassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(CARACTERES_PASSWORD.charAt(RANDOM.nextInt(CARACTERES_PASSWORD.length())));
        }
        return sb.toString();
    }

    private String generarApiKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
