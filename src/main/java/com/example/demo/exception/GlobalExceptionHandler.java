package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Recurso no encontrado -> HTTP 404
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Violacion de regla de negocio (ej: placa duplicada, sin documento) -> HTTP 400
    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaDeNegocio(ReglaDeNegocioException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Errores de validacion de @Valid (@NotBlank, @Pattern, etc.) -> HTTP 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        StringBuilder mensajes = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            mensajes.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ")
        );
        return construirRespuesta(HttpStatus.BAD_REQUEST, mensajes.toString());
    }

    // Cualquier otro error no controlado -> HTTP 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarGeneral(Exception ex) {
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        return new ResponseEntity<>(body, status);
    }
}