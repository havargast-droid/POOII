package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Trayecto;

@Repository
public interface TrayectoRepository extends JpaRepository<Trayecto, Long> {

    // Trayectos de una ruta, en orden de parada (servicio 1)
    List<Trayecto> findByCodigoRutaOrderByOrdenParadaAsc(String codigoRuta);

    // Codigos de ruta de un conductor especifico, agrupados (servicio 2)
    @Query("SELECT DISTINCT t.codigoRuta FROM Trayecto t WHERE t.persona.identificacion = :identificacion")
    List<String> findCodigosRutaPorIdentificacionConductor(@Param("identificacion") String identificacion);

    // Codigo de ruta + conductor agrupado, por placa de vehiculo (servicio 3)
    @Query("SELECT DISTINCT t.codigoRuta, t.persona FROM Trayecto t WHERE t.vehiculo.placa = :placa")
    List<Object[]> findRutaYConductorPorPlaca(@Param("placa") String placa);

    // Trayectos que aun no tienen lat/long (para la tarea programada de geocodificacion)
    List<Trayecto> findByLatitudIsNullOrLongitudIsNull();

    // Rutas donde el vehiculo no esta habilitado o el conductor esta restringido (servicio 4)
    @Query("SELECT DISTINCT t FROM Trayecto t " +
           "LEFT JOIN VehiculoPersona vp ON vp.vehiculo = t.vehiculo AND vp.persona = t.persona " +
           "WHERE EXISTS (SELECT vd FROM VehiculoDocumento vd WHERE vd.vehiculo = t.vehiculo " +
           "   AND vd.estadoDocumento <> 'Habilitado') " +
           "OR vp.estadoConductor = 'RO'")
    List<Trayecto> findRutasConVehiculoNoHabilitadoOConductorRestringido();

    // Maximo numero de paradas ya registradas para un codigo de ruta (validar limite de 5 intermedias)
    long countByCodigoRuta(String codigoRuta);
}
