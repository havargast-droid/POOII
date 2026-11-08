package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByIdentificacion(String identificacion);

    boolean existsByIdentificacion(String identificacion);

    List<Persona> findByTipoPersona(String tipoPersona);

    // Conteo de personas agrupadas por tipo (servicio publico)
    @Query("SELECT p.tipoPersona, COUNT(p) FROM Persona p GROUP BY p.tipoPersona")
    List<Object[]> contarPorTipoPersona();
}
