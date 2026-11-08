package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.VehiculoDocumento;

@Repository
public interface VehiculoDocumentoRepository extends JpaRepository<VehiculoDocumento, Long> {

    // Listar todos los documentos asociados a un vehiculo especifico
    List<VehiculoDocumento> findByVehiculoId(Long vehiculoId);
}