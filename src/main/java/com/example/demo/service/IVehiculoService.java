package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.Vehiculo;
import com.example.demo.entities.VehiculoDocumento;

public interface IVehiculoService {

    // CRUD
    Vehiculo crear(Vehiculo vehiculo);

    Vehiculo actualizar(Long id, Vehiculo vehiculo);

    void eliminar(Long id);

    List<Vehiculo> listar();

    Vehiculo buscarPorId(Long id);

    // BUSQUEDAS ESPECIFICAS
    Vehiculo buscarPorPlaca(String placa);

    List<Vehiculo> buscarPorTipoVehiculo(String tipoVehiculo);

    List<Vehiculo> buscarPorDocumentoComun(String codigoDocumento);

    List<Vehiculo> buscarPorEstadoDocumento(String estado);

    // AGREGAR DOCUMENTO A UN VEHICULO EXISTENTE
    Vehiculo agregarDocumento(Long vehiculoId, VehiculoDocumento nuevoDocumento);
}
