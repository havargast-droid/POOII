package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.VehiculoPersona;

@Repository
public interface VehiculoPersonaRepository extends JpaRepository<VehiculoPersona, Long> {

    List<VehiculoPersona> findByVehiculoId(Long vehiculoId);

    List<VehiculoPersona> findByPersonaId(Long personaId);

    List<VehiculoPersona> findByEstadoConductor(String estadoConductor);

    // Conductores (personas) distintos que pueden operar (servicio publico)
    @Query("SELECT DISTINCT vp.persona FROM VehiculoPersona vp WHERE vp.estadoConductor = 'PO'")
    List<com.example.demo.entities.Persona> findPersonasQuePuedenOperar();
}
