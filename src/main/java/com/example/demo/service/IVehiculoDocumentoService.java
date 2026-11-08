package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CargaDocumentoRequest;
import com.example.demo.entities.VehiculoDocumento;

public interface IVehiculoDocumentoService {

    List<VehiculoDocumento> cargarDocumentos(List<CargaDocumentoRequest> solicitudes);

    VehiculoDocumento cambiarEstado(Long id, String estado);
}
