package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Documento;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.DocumentoRepository;

@Service
public class DocumentoServiceImpl implements IDocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Override
    @Transactional
    public Documento crear(Documento documento) {
        if (documentoRepository.existsByCodigoDocumento(documento.getCodigoDocumento())) {
            throw new ReglaDeNegocioException(
                "Ya existe un documento con el codigo " + documento.getCodigoDocumento());
        }
        return documentoRepository.save(documento);
    }

    @Override
    @Transactional
    public Documento actualizar(Long id, Documento documento) {
        Documento existente = buscarPorId(id);

        existente.setCodigoDocumento(documento.getCodigoDocumento());
        existente.setNombreDocumento(documento.getNombreDocumento());
        existente.setTipoVehiculoAplica(documento.getTipoVehiculoAplica());
        existente.setObligatoriedad(documento.getObligatoriedad());
        existente.setDescripcion(documento.getDescripcion());

        return documentoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Documento existente = buscarPorId(id);
        documentoRepository.delete(existente);
    }

    @Override
    public List<Documento> listar() {
        return documentoRepository.findAll();
    }

    @Override
    public Documento buscarPorId(Long id) {
        return documentoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un documento con id " + id));
    }
}