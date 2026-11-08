package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.Documento;

public interface IDocumentoService {

    Documento crear(Documento documento);

    Documento actualizar(Long id, Documento documento);

    void eliminar(Long id);

    List<Documento> listar();

    Documento buscarPorId(Long id);
}
