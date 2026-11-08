package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Documento;
import com.example.demo.entities.Vehiculo;
import com.example.demo.entities.VehiculoDocumento;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.DocumentoRepository;
import com.example.demo.repository.VehiculoRepository;

@Service
public class VehiculoServiceImpl implements IVehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    // ================= CREAR =================
    @Override
    @Transactional
    public Vehiculo crear(Vehiculo vehiculo) {
        // Regla: no se puede crear un vehiculo sin al menos un documento asociado
        if (vehiculo.getDocumentos() == null || vehiculo.getDocumentos().isEmpty()) {
            throw new ReglaDeNegocioException(
                "No se puede crear un vehiculo sin al menos un documento asociado");
        }

        if (vehiculoRepository.existsByPlaca(vehiculo.getPlaca())) {
            throw new ReglaDeNegocioException(
                "Ya existe un vehiculo registrado con la placa " + vehiculo.getPlaca());
        }

        // Validar cada documento asociado y forzar estado inicial "En Verificacion"
        for (VehiculoDocumento vd : vehiculo.getDocumentos()) {
            Documento documento = documentoRepository.findById(vd.getDocumento().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "El documento con id " + vd.getDocumento().getId() + " no existe"));

            vd.setDocumento(documento);
            vd.setVehiculo(vehiculo);
            vd.setEstadoDocumento("En Verificacion");
        }

        return vehiculoRepository.save(vehiculo);
    }

    // ================= ACTUALIZAR =================
    @Override
    @Transactional
    public Vehiculo actualizar(Long id, Vehiculo vehiculo) {
        Vehiculo existente = buscarPorId(id);

        existente.setTipoVehiculo(vehiculo.getTipoVehiculo());
        existente.setPlaca(vehiculo.getPlaca());
        existente.setTipoServicio(vehiculo.getTipoServicio());
        existente.setTipoCombustible(vehiculo.getTipoCombustible());
        existente.setCapacidadPasajeros(vehiculo.getCapacidadPasajeros());
        existente.setColor(vehiculo.getColor());
        existente.setModelo(vehiculo.getModelo());
        existente.setMarca(vehiculo.getMarca());
        existente.setLinea(vehiculo.getLinea());

        return vehiculoRepository.save(existente);
    }

    // ================= ELIMINAR =================
    @Override
    @Transactional
    public void eliminar(Long id) {
        Vehiculo existente = buscarPorId(id);
        vehiculoRepository.delete(existente);
    }

    // ================= LISTAR =================
    @Override
    public List<Vehiculo> listar() {
        return vehiculoRepository.findAll();
    }

    // ================= BUSCAR POR ID =================
    @Override
    public Vehiculo buscarPorId(Long id) {
        return vehiculoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un vehiculo con id " + id));
    }

    // ================= BUSCAR POR PLACA =================
    @Override
    public Vehiculo buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un vehiculo con placa " + placa));
    }

    // ================= BUSCAR POR TIPO =================
    @Override
    public List<Vehiculo> buscarPorTipoVehiculo(String tipoVehiculo) {
        return vehiculoRepository.findByTipoVehiculo(tipoVehiculo);
    }

    // ================= BUSCAR POR DOCUMENTO EN COMUN =================
    @Override
    public List<Vehiculo> buscarPorDocumentoComun(String codigoDocumento) {
        return vehiculoRepository.findByDocumentoCodigo(codigoDocumento);
    }

    // ================= BUSCAR POR ESTADO DE DOCUMENTO =================
    @Override
    public List<Vehiculo> buscarPorEstadoDocumento(String estado) {
        return vehiculoRepository.findByEstadoDocumento(estado);
    }

    // ================= AGREGAR DOCUMENTO A VEHICULO EXISTENTE =================
    @Override
    @Transactional
    public Vehiculo agregarDocumento(Long vehiculoId, VehiculoDocumento nuevoDocumento) {
        Vehiculo vehiculo = buscarPorId(vehiculoId);

        if (nuevoDocumento.getDocumento() == null || nuevoDocumento.getDocumento().getId() == null) {
            throw new ReglaDeNegocioException("Debe indicar el documento: {\"documento\": {\"id\": N}}");
        }

        Documento documento = documentoRepository.findById(nuevoDocumento.getDocumento().getId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "El documento con id " + nuevoDocumento.getDocumento().getId() + " no existe"));

        nuevoDocumento.setVehiculo(vehiculo);
        nuevoDocumento.setDocumento(documento);

        if (nuevoDocumento.getEstadoDocumento() == null) {
            nuevoDocumento.setEstadoDocumento("En Verificacion");
        }

        if (nuevoDocumento.getFechaExpedicion() == null) {
            nuevoDocumento.setFechaExpedicion(LocalDate.now());
        }

        vehiculo.getDocumentos().add(nuevoDocumento);

        return vehiculoRepository.save(vehiculo);
    }
}
