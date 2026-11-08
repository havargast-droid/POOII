package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.TrayectoTmp;

@Repository
public interface TrayectoTmpRepository extends JpaRepository<TrayectoTmp, Long> {

    List<TrayectoTmp> findByIdCargueOrderByIdAsc(Long idCargue);

    boolean existsByIdCargue(Long idCargue);

    long countByIdCargueAndEstado(Long idCargue, String estado);

    // === Stored procedures (el nombre del metodo = nombre del procedimiento en MySQL) ===

    @Procedure(procedureName = "CARGAR_DATOS_TRAYECTO")
    void CARGAR_DATOS_TRAYECTO(String identificacion, String placa, String codigoRuta,
            String ubicacion, String ordenParada, String login, Long idCargue);

    @Procedure(procedureName = "VALIDAR_DATOS_TRAYECTO")
    void VALIDAR_DATOS_TRAYECTO(Long idCargue);

    @Procedure(procedureName = "PROCESAR_DATOS_TRAYECTO")
    void PROCESAR_DATOS_TRAYECTO(Long idCargue);
}
