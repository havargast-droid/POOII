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
import com.example.demo.entities.PersonaLab;
import com.example.demo.entities.PersonaTmp;
import com.example.demo.exception.ReglaDeNegocioException;
import com.example.demo.repository.PersonaLabRepository;
import com.example.demo.repository.PersonaTmpRepository;

@Service
public class PersonaCargueServiceImpl implements IPersonaCargueService {

    private static final Logger logger = LoggerFactory.getLogger(PersonaCargueServiceImpl.class);
    private static final DateTimeFormatter FORMATO_ID_CARGUE = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    @Autowired
    private PersonaTmpRepository tmpRepository;

    @Autowired
    private PersonaLabRepository personaLabRepository;

    // Plantilla: A NOMBRE | B EDAD | C UBICACION (fila 1 = encabezados)
    @Override
    public CargueResultado procesarExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ReglaDeNegocioException("Debe enviar un archivo Excel (campo 'file')");
        }
        Long idCargue = Long.parseLong(LocalDateTime.now().format(FORMATO_ID_CARGUE));
        if (tmpRepository.existsByIdCargue(idCargue)) {
            throw new ReglaDeNegocioException("Ya se hizo un cargue en este mismo minuto (idCargue " + idCargue
                    + "). Espere un minuto y vuelva a intentarlo.");
        }

        CargueResultado resultado = new CargueResultado();
        resultado.setIdCargue(idCargue);
        DataFormatter formatter = new DataFormatter();

        try (InputStream in = file.getInputStream(); Workbook workbook = WorkbookFactory.create(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                String nombre = texto(formatter, row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
                String edad = texto(formatter, row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
                String ubicacion = texto(formatter, row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
                if (nombre == null && edad == null && ubicacion == null) {
                    continue;
                }
                resultado.setFilasLeidas(resultado.getFilasLeidas() + 1);
                try {
                    tmpRepository.CARGAR_DATOS_EXCEL(nombre, edad, ubicacion, idCargue);
                    resultado.setFilasCargadas(resultado.getFilasCargadas() + 1);
                } catch (Exception e) {
                    logger.error("No se pudo cargar la fila {}: {}", row.getRowNum() + 1, e.getMessage());
                    resultado.getFilasNoCargadas().add("Fila " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new ReglaDeNegocioException("No se pudo leer el archivo Excel: " + e.getMessage());
        } catch (ReglaDeNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new ReglaDeNegocioException("El archivo no es un Excel valido: " + e.getMessage());
        }

        if (resultado.getFilasCargadas() > 0) {
            tmpRepository.VALIDAR_DATOS_CARGUE(idCargue);
            tmpRepository.PROCESAR_DATOS_EXCEL(idCargue);
        }
        resultado.setProcesados(tmpRepository.countByIdCargueAndEstado(idCargue, "PROCESADO"));
        resultado.setConError(tmpRepository.countByIdCargueAndEstado(idCargue, "ERROR"));
        resultado.setMensaje("Cargue " + idCargue + " finalizado. Consulte GET /persona-cargue/" + idCargue + " para el detalle.");
        return resultado;
    }

    @Override
    public List<PersonaTmp> consultarPorIdCargue(Long idCargue) {
        return tmpRepository.findByIdCargueOrderByIdAsc(idCargue);
    }

    @Override
    public List<PersonaLab> listarPersonasCargadas() {
        return personaLabRepository.findAll();
    }

    private String texto(DataFormatter formatter, Cell cell) {
        if (cell == null) {
            return null;
        }
        String valor = formatter.formatCellValue(cell);
        return (valor == null || valor.trim().isEmpty()) ? null : valor.trim();
    }
}
