package com.example.demo.service;

import com.example.demo.entities.Usuario;

public interface IUsuarioService {

    void cambiarPassword(String login, String nuevoPassword);

    String regenerarApiKey(String login);

    Usuario buscarPorLogin(String login);
}
