package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado del cargue masivo de trayectos")
public class CargueResultado {

    @Schema(description = "Id del cargue (yyyyMMddHHmm). Sirve para consultar el detalle despues")
    private Long idCargue;
    @Schema(description = "Filas con datos leidas del Excel")
    private int filasLeidas;
    @Schema(description = "Filas copiadas a la tabla temporal")
    private int filasCargadas;
    @Schema(description = "Registros que quedaron PROCESADOS (pasaron a la tabla trayecto)")
    private long procesados;
    @Schema(description = "Registros que quedaron en ERROR (ver observacion)")
    private long conError;
    @Schema(description = "Filas del Excel que no se pudieron copiar a la tabla temporal")
    private List<String> filasNoCargadas = new ArrayList<>();
    @Schema(description = "Mensaje resumen")
    private String mensaje;

    public Long getIdCargue() { return idCargue; }
    public void setIdCargue(Long idCargue) { this.idCargue = idCargue; }
    public int getFilasLeidas() { return filasLeidas; }
    public void setFilasLeidas(int filasLeidas) { this.filasLeidas = filasLeidas; }
    public int getFilasCargadas() { return filasCargadas; }
    public void setFilasCargadas(int filasCargadas) { this.filasCargadas = filasCargadas; }
    public long getProcesados() { return procesados; }
    public void setProcesados(long procesados) { this.procesados = procesados; }
    public long getConError() { return conError; }
    public void setConError(long conError) { this.conError = conError; }
    public List<String> getFilasNoCargadas() { return filasNoCargadas; }
    public void setFilasNoCargadas(List<String> f) { this.filasNoCargadas = f; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
