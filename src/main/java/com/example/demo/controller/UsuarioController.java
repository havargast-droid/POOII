package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CambioPasswordRequest;
import com.example.demo.service.IUsuarioService;

@Tag(name = "Usuario")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Operation(summary = "Cambiar password")
    @PutMapping("/{login}/password")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @PathVariable String login, @RequestBody CambioPasswordRequest request) {
        usuarioService.cambiarPassword(login, request.getPassword());
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Password actualizado correctamente");
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Regenerar api key")
    @GetMapping("/{login}/apikey")
    public ResponseEntity<Map<String, String>> regenerarApiKey(@PathVariable String login) {
        String nuevaApiKey = usuarioService.regenerarApiKey(login);
        Map<String, String> body = new HashMap<>();
        body.put("apikey", nuevaApiKey);
        return ResponseEntity.ok(body);
    }
}
