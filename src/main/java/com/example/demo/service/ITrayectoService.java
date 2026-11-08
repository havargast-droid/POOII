package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CrearTrayectoRequest;
import com.example.demo.entities.Trayecto;

public interface ITrayectoService {

    Trayecto crear(CrearTrayectoRequest request, String loginUsuario);

    List<Trayecto> consultarPorCodigoRuta(String codigoRuta);

    List<String> consultarCodigosRutaPorConductor(String identificacion);

    List<Object[]> consultarRutaYConductorPorPlaca(String placa);

    List<Trayecto> consultarRutasProblematicas();
}
