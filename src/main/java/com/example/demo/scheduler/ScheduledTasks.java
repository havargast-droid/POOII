package com.example.demo.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.apis.googlemaps.Geocoder;
import com.example.demo.entities.Persona;
import com.example.demo.entities.Trayecto;
import com.example.demo.entities.VehiculoDocumento;
import com.example.demo.entities.VehiculoPersona;
import com.example.demo.repository.PersonaRepository;
import com.example.demo.repository.TrayectoRepository;
import com.example.demo.repository.VehiculoDocumentoRepository;
import com.example.demo.repository.VehiculoPersonaRepository;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private VehiculoPersonaRepository vehiculoPersonaRepository;

    @Autowired
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;

    @Autowired
    private TrayectoRepository trayectoRepository;

    // === Tarea 1: cada 2 minutos, licencias vencidas -> estado conductor = RO ===
    @Scheduled(fixedRate = 120000)
    public void verificarLicenciasVencidas() {
        logger.info("[Tarea Licencias] Ejecucion - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            List<Persona> conductores = personaRepository.findByTipoPersona("C");
            LocalDate hoy = LocalDate.now();

            for (Persona conductor : conductores) {
                LocalDate vigencia = conductor.getFechaVigenciaLicencia();
                if (vigencia != null && vigencia.isBefore(hoy)) {
                    List<VehiculoPersona> relaciones = vehiculoPersonaRepository.findByPersonaId(conductor.getId());
                    for (VehiculoPersona vp : relaciones) {
                        if (!"RO".equals(vp.getEstadoConductor())) {
                            vp.setEstadoConductor("RO");
                            vehiculoPersonaRepository.save(vp);
                            logger.info("Conductor {} restringido por licencia vencida (vigencia {})",
                                conductor.getIdentificacion(), vigencia);
                            // BONUS opcional: aqui se podria enviar un correo electronico al conductor
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error en tarea de verificacion de licencias: {}", e.getMessage());
        }
    }

    // === Tarea 2: cada 2 minutos, documentos vencidos -> estado = Vencido ===
    @Scheduled(fixedRate = 120000)
    public void verificarDocumentosVencidos() {
        logger.info("[Tarea Documentos] Ejecucion - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            List<VehiculoDocumento> documentos = vehiculoDocumentoRepository.findAll();
            LocalDate hoy = LocalDate.now();

            for (VehiculoDocumento vd : documentos) {
                if (vd.getFechaVencimiento() != null
                        && vd.getFechaVencimiento().isBefore(hoy)
                        && !"Vencido".equals(vd.getEstadoDocumento())) {
                    vd.setEstadoDocumento("Vencido");
                    vehiculoDocumentoRepository.save(vd);
                    logger.info("Documento id {} marcado como Vencido", vd.getId());
                }
            }
        } catch (Exception e) {
            logger.error("Error en tarea de verificacion de documentos: {}", e.getMessage());
        }
    }

    // === Tarea 3: cada 90 segundos, geocodificar trayectos sin lat/long ===
    @Scheduled(fixedRate = 90000)
    public void geocodificarTrayectos() {
        logger.info("[Tarea Geocodificacion] Ejecucion - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            List<Trayecto> pendientes = trayectoRepository.findByLatitudIsNullOrLongitudIsNull();
            if (pendientes.isEmpty()) {
                return;
            }

            Geocoder geocoder = new Geocoder();
            for (Trayecto trayecto : pendientes) {
                try {
                    String latLng = geocoder.getLatLng(trayecto.getUbicacion());
                    String[] coor = latLng.split(",");
                    trayecto.setLatitud(Double.parseDouble(coor[0]));
                    trayecto.setLongitud(Double.parseDouble(coor[1]));
                    trayectoRepository.save(trayecto);
                    logger.info("Trayecto {} geocodificado: {}", trayecto.getId(), latLng);
                } catch (Exception exTrayecto) {
                    logger.error("No se pudo geocodificar el trayecto {}: {}",
                        trayecto.getId(), exTrayecto.getMessage());
                }
                // Nominatim permite maximo 1 consulta por segundo
                Thread.sleep(1100);
            }
        } catch (Exception e) {
            logger.error("Error en tarea de geocodificacion: {}", e.getMessage());
        }
    }
}
