package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    // Buscar documento por su codigo parametrizado (unico)
    Optional<Documento> findByCodigoDocumento(String codigoDocumento);

    // Verificar existencia de un codigo (util para validar duplicados en el Service)
    boolean existsByCodigoDocumento(String codigoDocumento);
}
