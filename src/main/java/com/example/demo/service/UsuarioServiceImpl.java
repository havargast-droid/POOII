package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Usuario;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void cambiarPassword(String login, String nuevoPassword) {
        Usuario usuario = buscarPorLogin(login);
        usuario.setPassword(nuevoPassword);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public String regenerarApiKey(String login) {
        Usuario usuario = buscarPorLogin(login);
        String nuevaApiKey = UUID.randomUUID().toString().replace("-", "");
        usuario.setApikey(nuevaApiKey);
        usuarioRepository.save(usuario);
        return nuevaApiKey;
    }

    @Override
    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con login " + login));
    }
}
