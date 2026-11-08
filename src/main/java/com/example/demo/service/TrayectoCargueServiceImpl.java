package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CargueResultado;
import com.example.demo.entities.TrayectoTmp;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.TrayectoTmpRepository;

@Service
public class TrayectoCargueServiceImpl implements ITrayectoCargueService {

    private static final Logger logger = LoggerFactory.getLogger(TrayectoCargueServiceImpl.class);
    private static final DateTimeFormatter FORMATO_ID_CARGUE = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    @Autowired
    private TrayectoTmpRepository tmpRepository;

    /*
     * Plantilla del Excel (primera hoja, fila 1 = encabezados, datos desde la fila 2):
     *   A: IDENTIFICACION_CONDUCTOR | B: PLACA | C: CODIGO_RUTA | D: UBICACION | E: ORDEN_PARADA
     */
    @Override
    public CargueResultado procesarExcel(MultipartFile file, String login) {
        if (file == null || file.isEmpty()) {
            throw new ReglaDeNegocioException("Debe enviar un archivo Excel (campo 'file')");
        }

        Long idCargue = Long.parseLong(LocalDateTime.now().format(FORMATO_ID_CARGUE));
        if (tmpRepository.existsByIdCargue(idCargue)) {
            throw new ReglaDeNegocioException("Ya se hizo un cargue en este mismo minuto (idCargue " + idCargue
                    + "). Espere un minuto y vuelva a intentarlo, sin pulsar Execute dos veces.");
        }
        CargueResultado resultado = new CargueResultado();
        resultado.setIdCargue(idCargue);

        DataFormatter formatter = new DataFormatter();

        try (InputStream in = file.getInputStream(); Workbook workbook = WorkbookFactory.create(in)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // encabezados
                }
                String identificacion = texto(formatter, row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
                String placa = texto(formatter, row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
                String codigoRuta = texto(formatter, row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
                String ubicacion = texto(formatter, row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
                String ordenParada = texto(formatter, row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));

                // fila totalmente vacia: se ignora
                if (identificacion == null && placa == null && codigoRuta == null
                        && ubicacion == null && ordenParada == null) {
                    continue;
                }
                resultado.setFilasLeidas(resultado.getFilasLeidas() + 1);

                try {
                    tmpRepository.CARGAR_DATOS_TRAYECTO(identificacion, placa, codigoRuta,
                            ubicacion, ordenParada, login, idCargue);
                    resultado.setFilasCargadas(resultado.getFilasCargadas() + 1);
                } catch (Exception e) {
                    logger.error("No se pudo cargar la fila {}: {}", row.getRowNum() + 1, e.getMessage());
                    resultado.getFilasNoCargadas().add("Fila " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new ReglaDeNegocioException("No se pudo leer el archivo Excel: " + e.getMessage());
        } catch (Exception e) {
            if (e instanceof ReglaDeNegocioException) {
                throw (ReglaDeNegocioException) e;
            }
            throw new ReglaDeNegocioException("El archivo no es un Excel valido: " + e.getMessage());
        }

        if (resultado.getFilasCargadas() > 0) {
            tmpRepository.VALIDAR_DATOS_TRAYECTO(idCargue);
            tmpRepository.PROCESAR_DATOS_TRAYECTO(idCargue);
        }

        resultado.setProcesados(tmpRepository.countByIdCargueAndEstado(idCargue, "PROCESADO"));
        resultado.setConError(tmpRepository.countByIdCargueAndEstado(idCargue, "ERROR"));
        resultado.setMensaje("Cargue " + idCargue + " finalizado. Consulte GET /trayecto/cargue/" + idCargue
                + " para ver el detalle y las observaciones.");
        return resultado;
    }

    @Override
    public List<TrayectoTmp> consultarPorIdCargue(Long idCargue) {
        return tmpRepository.findByIdCargueOrderByIdAsc(idCargue);
    }

    private String texto(DataFormatter formatter, Cell cell) {
        if (cell == null) {
            return null;
        }
        String valor = formatter.formatCellValue(cell);
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }
}
