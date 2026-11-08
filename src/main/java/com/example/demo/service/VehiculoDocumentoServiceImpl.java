package com.example.demo.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CargaDocumentoRequest;
import com.example.demo.entities.Documento;
import com.example.demo.entities.Vehiculo;
import com.example.demo.entities.VehiculoDocumento;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.DocumentoRepository;
import com.example.demo.repository.VehiculoDocumentoRepository;
import com.example.demo.repository.VehiculoRepository;

@Service
public class VehiculoDocumentoServiceImpl implements IVehiculoDocumentoService {

    @Autowired
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Override
    @Transactional
    public List<VehiculoDocumento> cargarDocumentos(List<CargaDocumentoRequest> solicitudes) {
        if (solicitudes == null || solicitudes.isEmpty()) {
            throw new ReglaDeNegocioException("Debe enviar al menos un documento para cargar");
        }

        List<VehiculoDocumento> resultado = new ArrayList<>();

        for (CargaDocumentoRequest req : solicitudes) {
            Vehiculo vehiculo = vehiculoRepository.findById(req.getVehiculoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No existe un vehiculo con id " + req.getVehiculoId()));

            Documento documento = documentoRepository.findById(req.getDocumentoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No existe un documento con id " + req.getDocumentoId()));

            byte[] archivo;
            try {
                archivo = Base64.getDecoder().decode(req.getArchivoBase64());
            } catch (IllegalArgumentException e) {
                throw new ReglaDeNegocioException(
                    "El archivo enviado para el documento " + req.getDocumentoId() + " no es Base64 valido");
            }

            // Busca si ya existe la relacion vehiculo-documento para actualizarla, si no, crea una nueva
            VehiculoDocumento vd = vehiculoDocumentoRepository.findByVehiculoId(vehiculo.getId()).stream()
                .filter(x -> x.getDocumento().getId().equals(documento.getId()))
                .findFirst()
                .orElse(new VehiculoDocumento());

            vd.setVehiculo(vehiculo);
            vd.setDocumento(documento);
            vd.setArchivoDocumento(archivo);
            vd.setFechaExpedicion(req.getFechaExpedicion());
            vd.setFechaVencimiento(req.getFechaVencimiento());
            if (vd.getEstadoDocumento() == null) {
                vd.setEstadoDocumento("En Verificacion");
            }

            resultado.add(vehiculoDocumentoRepository.save(vd));
        }

        return resultado;
    }

    @Override
    public VehiculoDocumento cambiarEstado(Long id, String estado) {
        VehiculoDocumento vd = vehiculoDocumentoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("El documento de vehiculo con id " + id + " no existe"));

        if (estado == null || !estado.matches("Habilitado|Vencido|En Verificacion")) {
            throw new ReglaDeNegocioException("Estado invalido. Use: Habilitado, Vencido o En Verificacion");
        }
        if ("Habilitado".equals(estado) && vd.getFechaVencimiento() != null
                && vd.getFechaVencimiento().isBefore(java.time.LocalDate.now())) {
            throw new ReglaDeNegocioException("No se puede habilitar: el documento ya esta vencido");
        }
        vd.setEstadoDocumento(estado);
        return vehiculoDocumentoRepository.save(vd);
    }
}
