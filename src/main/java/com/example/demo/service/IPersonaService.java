package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.Persona;

public interface IPersonaService {

    Persona crear(Persona persona);

    Persona actualizar(Long id, Persona persona);

    List<Persona> listar();

    Persona buscarPorId(Long id);

    List<Object[]> contarPorTipo();
}
