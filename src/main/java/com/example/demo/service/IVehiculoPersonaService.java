package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.VehiculoPersona;

public interface IVehiculoPersonaService {

    VehiculoPersona asociarConductor(Long vehiculoId, Long personaId);

    VehiculoPersona cambiarEstado(Long id, String nuevoEstado);

    List<VehiculoPersona> listarPorVehiculo(Long vehiculoId);

    List<com.example.demo.entities.Persona> listarConductoresQuePuedenOperar();
}
