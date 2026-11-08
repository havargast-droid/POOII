-- =====================================================================
-- ENTREGA 4 - Cargue masivo de trayectos desde Excel (Stored Procedures)
-- Ejecutar en MySQL Workbench sobre la base proyecto_vehiculos.
-- Se puede ejecutar completo varias veces (usa IF NOT EXISTS / DROP IF EXISTS).
-- =====================================================================
USE proyecto_vehiculos;

-- ---------------------------------------------------------------------
-- 1) Tabla temporal de cargue (mismos campos de trayecto, en texto)
--    OJO: si ya arrancaste la app con la entidad TrayectoTmp, Hibernate
--    ya la creo; esta sentencia simplemente no hace nada.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS trayecto_tmp (
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    identificacion_conductor  VARCHAR(255),
    placa                     VARCHAR(255),
    codigo_ruta               VARCHAR(255),
    ubicacion                 VARCHAR(255),
    orden_parada              VARCHAR(255),
    latitud                   VARCHAR(255),
    longitud                  VARCHAR(255),
    login_usuario             VARCHAR(255),
    estado                    VARCHAR(10) NOT NULL,
    observacion               VARCHAR(1000),
    id_cargue                 BIGINT NOT NULL,
    CONSTRAINT chk_trayecto_tmp_estado
        CHECK (estado IN ('CARGADO','VALIDADO','PROCESADO','ERROR'))
);

-- ---------------------------------------------------------------------
-- 2) Procedimiento de CARGUE: copia la fila del Excel sin alterarla
-- ---------------------------------------------------------------------
DROP PROCEDURE IF EXISTS CARGAR_DATOS_TRAYECTO;
DELIMITER //
CREATE PROCEDURE CARGAR_DATOS_TRAYECTO(
    IN p_identificacion VARCHAR(255),
    IN p_placa          VARCHAR(255),
    IN p_codigo_ruta    VARCHAR(255),
    IN p_ubicacion      VARCHAR(255),
    IN p_orden_parada   VARCHAR(255),
    IN p_login          VARCHAR(255),
    IN p_id_cargue      BIGINT
)
BEGIN
    INSERT INTO trayecto_tmp
        (identificacion_conductor, placa, codigo_ruta, ubicacion, orden_parada,
         login_usuario, estado, observacion, id_cargue)
    VALUES
        (p_identificacion, p_placa, p_codigo_ruta, p_ubicacion, p_orden_parada,
         p_login, 'CARGADO', '', p_id_cargue);
END //
DELIMITER ;

-- ---------------------------------------------------------------------
-- 3) Procedimiento de VALIDACION: marca ERROR acumulando observaciones
--    Reglas = las mismas del servicio de crear trayecto (Entregas 1-3)
-- ---------------------------------------------------------------------
DROP PROCEDURE IF EXISTS VALIDAR_DATOS_TRAYECTO;
DELIMITER //
CREATE PROCEDURE VALIDAR_DATOS_TRAYECTO(IN p_id_cargue BIGINT)
BEGIN
    -- R1: campos obligatorios
    UPDATE trayecto_tmp
       SET estado = 'ERROR',
           observacion = CONCAT(observacion, ' Identificacion del conductor es obligatoria.')
     WHERE id_cargue = p_id_cargue
       AND (identificacion_conductor IS NULL OR TRIM(identificacion_conductor) = '');

    UPDATE trayecto_tmp
       SET estado = 'ERROR',
           observacion = CONCAT(observacion, ' La placa es obligatoria.')
     WHERE id_cargue = p_id_cargue
       AND (placa IS NULL OR TRIM(placa) = '');

    UPDATE trayecto_tmp
       SET estado = 'ERROR',
           observacion = CONCAT(observacion, ' El codigo de ruta es obligatorio.')
     WHERE id_cargue = p_id_cargue
       AND (codigo_ruta IS NULL OR TRIM(codigo_ruta) = '');

    UPDATE trayecto_tmp
       SET estado = 'ERROR',
           observacion = CONCAT(observacion, ' La ubicacion es obligatoria.')
     WHERE id_cargue = p_id_cargue
       AND (ubicacion IS NULL OR TRIM(ubicacion) = '');

    -- R2: orden de parada numerico (entero entre 0 y 999)
    UPDATE trayecto_tmp
       SET estado = 'ERROR',
           observacion = CONCAT(observacion, ' El orden de parada debe ser un numero entero.')
     WHERE id_cargue = p_id_cargue
       AND (orden_parada IS NULL OR TRIM(orden_parada) NOT REGEXP '^[0-9]{1,3}$');

    -- R3: el conductor debe existir
    UPDATE trayecto_tmp t
       SET t.estado = 'ERROR',
           t.observacion = CONCAT(t.observacion, ' El conductor no existe.')
     WHERE t.id_cargue = p_id_cargue
       AND t.identificacion_conductor IS NOT NULL AND TRIM(t.identificacion_conductor) <> ''
       AND NOT EXISTS (SELECT 1 FROM persona p WHERE p.identificacion = TRIM(t.identificacion_conductor));

    -- R4: la persona debe ser de tipo conductor (C)
    UPDATE trayecto_tmp t
       SET t.estado = 'ERROR',
           t.observacion = CONCAT(t.observacion, ' La persona no es de tipo Conductor.')
     WHERE t.id_cargue = p_id_cargue
       AND EXISTS (SELECT 1 FROM persona p
                    WHERE p.identificacion = TRIM(t.identificacion_conductor)
                      AND p.tipo_persona <> 'C');

    -- R5: el vehiculo debe existir (por placa)
    UPDATE trayecto_tmp t
       SET t.estado = 'ERROR',
           t.observacion = CONCAT(t.observacion, ' El vehiculo no existe.')
     WHERE t.id_cargue = p_id_cargue
       AND t.placa IS NOT NULL AND TRIM(t.placa) <> ''
       AND NOT EXISTS (SELECT 1 FROM vehiculo v WHERE v.placa = UPPER(TRIM(t.placa)));

    -- R6: el vehiculo debe tener documentos y TODOS en estado Habilitado
    UPDATE trayecto_tmp t
       SET t.estado = 'ERROR',
           t.observacion = CONCAT(t.observacion, ' El vehiculo no tiene todos sus documentos Habilitados.')
     WHERE t.id_cargue = p_id_cargue
       AND EXISTS (SELECT 1 FROM vehiculo v WHERE v.placa = UPPER(TRIM(t.placa)))
       AND ( NOT EXISTS (SELECT 1 FROM vehiculo v
                           JOIN vehiculo_documento vd ON vd.vehiculo_id = v.id
                          WHERE v.placa = UPPER(TRIM(t.placa)))
          OR EXISTS (SELECT 1 FROM vehiculo v
                       JOIN vehiculo_documento vd ON vd.vehiculo_id = v.id
                      WHERE v.placa = UPPER(TRIM(t.placa))
                        AND vd.estado_documento <> 'Habilitado') );

    -- R7: el conductor debe estar asociado al vehiculo con estado PO
    UPDATE trayecto_tmp t
       SET t.estado = 'ERROR',
           t.observacion = CONCAT(t.observacion, ' El conductor no esta asociado al vehiculo con estado PO.')
     WHERE t.id_cargue = p_id_cargue
       AND EXISTS (SELECT 1 FROM persona p WHERE p.identificacion = TRIM(t.identificacion_conductor))
       AND EXISTS (SELECT 1 FROM vehiculo v WHERE v.placa = UPPER(TRIM(t.placa)))
       AND NOT EXISTS (SELECT 1
                         FROM vehiculo_persona vp
                         JOIN persona p ON p.id = vp.persona_id
                         JOIN vehiculo v ON v.id = vp.vehiculo_id
                        WHERE p.identificacion = TRIM(t.identificacion_conductor)
                          AND v.placa = UPPER(TRIM(t.placa))
                          AND vp.estado_conductor = 'PO');

    -- R8: maximo 7 trayectos por ruta (1 inicial + 5 intermedias + 1 final)
    --     = los ya guardados en trayecto + los de este cargue
    --     (se usan tablas derivadas para evitar el error 1093 de MySQL)
    UPDATE trayecto_tmp t
      JOIN (SELECT codigo_ruta, COUNT(*) AS total
              FROM trayecto_tmp
             WHERE id_cargue = p_id_cargue
             GROUP BY codigo_ruta) c ON c.codigo_ruta = t.codigo_ruta
      LEFT JOIN (SELECT codigo_ruta, COUNT(*) AS total
                   FROM trayecto
                  GROUP BY codigo_ruta) e ON e.codigo_ruta = t.codigo_ruta
       SET t.estado = 'ERROR',
           t.observacion = CONCAT(t.observacion, ' La ruta supera el maximo de 7 trayectos.')
     WHERE t.id_cargue = p_id_cargue
       AND (c.total + IFNULL(e.total, 0)) > 7;

    -- R9: no repetir el orden de parada dentro de la misma ruta (en el cargue)
    UPDATE trayecto_tmp t
      JOIN (SELECT codigo_ruta, orden_parada
              FROM trayecto_tmp
             WHERE id_cargue = p_id_cargue
             GROUP BY codigo_ruta, orden_parada
            HAVING COUNT(*) > 1) d
        ON d.codigo_ruta = t.codigo_ruta AND d.orden_parada = t.orden_parada
       SET t.estado = 'ERROR',
           t.observacion = CONCAT(t.observacion, ' Orden de parada repetido en la ruta.')
     WHERE t.id_cargue = p_id_cargue;

    -- Los que no acumularon ninguna observacion quedan VALIDADOS
    UPDATE trayecto_tmp
       SET estado = 'VALIDADO'
     WHERE id_cargue = p_id_cargue
       AND (observacion IS NULL OR observacion = '');
END //
DELIMITER ;

-- ---------------------------------------------------------------------
-- 4) Procedimiento de PROCESADO: pasa los VALIDADO a la tabla trayecto
--    (latitud/longitud las llena luego la tarea programada de geocodificacion)
-- ---------------------------------------------------------------------
DROP PROCEDURE IF EXISTS PROCESAR_DATOS_TRAYECTO;
DELIMITER //
CREATE PROCEDURE PROCESAR_DATOS_TRAYECTO(IN p_id_cargue BIGINT)
BEGIN
    INSERT INTO trayecto (idpersona, idvehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
    SELECT p.id, v.id, TRIM(t.codigo_ruta), TRIM(t.ubicacion),
           CAST(TRIM(t.orden_parada) AS UNSIGNED), t.login_usuario
      FROM trayecto_tmp t
      JOIN persona  p ON p.identificacion = TRIM(t.identificacion_conductor)
      JOIN vehiculo v ON v.placa = UPPER(TRIM(t.placa))
     WHERE t.id_cargue = p_id_cargue
       AND t.estado = 'VALIDADO';

    UPDATE trayecto_tmp
       SET estado = 'PROCESADO'
     WHERE id_cargue = p_id_cargue
       AND estado = 'VALIDADO';
END //
DELIMITER ;

-- Para revisar:  SELECT * FROM trayecto_tmp ORDER BY id DESC;
