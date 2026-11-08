package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Vehiculo;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Buscar vehiculo por placa (unico)
    Optional<Vehiculo> findByPlaca(String placa);

    // Buscar vehiculos por tipo (AUTOMOVIL / MOTOCICLETA)
    List<Vehiculo> findByTipoVehiculo(String tipoVehiculo);

    // Verificar si ya existe una placa (util para validaciones en el Service)
    boolean existsByPlaca(String placa);

    // Buscar vehiculos que tengan asociado un documento con un codigo especifico
    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN v.documentos vd " +
           "WHERE vd.documento.codigoDocumento = :codigoDocumento")
    List<Vehiculo> findByDocumentoCodigo(@Param("codigoDocumento") String codigoDocumento);

    // Buscar vehiculos segun el estado de alguno de sus documentos asociados
    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN v.documentos vd " +
           "WHERE vd.estadoDocumento = :estado")
    List<Vehiculo> findByEstadoDocumento(@Param("estado") String estado);

    // === Entrega 2: servicios publicos ===

    // Vehiculos con al menos un documento vencido
    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN v.documentos vd WHERE vd.estadoDocumento = 'Vencido'")
    List<Vehiculo> findVehiculosConDocumentosVencidos();

    // Vehiculos con documentos por vencer dentro de X dias desde hoy
    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN v.documentos vd " +
           "WHERE vd.fechaVencimiento BETWEEN CURRENT_DATE AND :fechaLimite")
    List<Vehiculo> findVehiculosConDocumentosPorVencer(@Param("fechaLimite") LocalDate fechaLimite);
}
