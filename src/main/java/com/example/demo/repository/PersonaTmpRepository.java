package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.PersonaTmp;

@Repository
public interface PersonaTmpRepository extends JpaRepository<PersonaTmp, Long> {

    List<PersonaTmp> findByIdCargueOrderByIdAsc(Long idCargue);

    boolean existsByIdCargue(Long idCargue);

    long countByIdCargueAndEstado(Long idCargue, String estado);

    @Procedure(procedureName = "CARGAR_DATOS_EXCEL")
    void CARGAR_DATOS_EXCEL(String pnombre, String edad, String ubicacion, Long idCargue);

    @Procedure(procedureName = "VALIDAR_DATOS_CARGUE")
    void VALIDAR_DATOS_CARGUE(Long idCargue);

    @Procedure(procedureName = "PROCESAR_DATOS_EXCEL")
    void PROCESAR_DATOS_EXCEL(Long idCargue);
}
