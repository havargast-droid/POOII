-- =====================================================================
-- LABORATORIO 4 COMPLEMENTARIO - Cargue masivo de personas desde Excel
-- Ejecutar en MySQL Workbench sobre proyecto_vehiculos.
-- (Usa persona_lab para no mezclarse con la tabla persona del proyecto.)
-- =====================================================================
USE proyecto_vehiculos;

CREATE TABLE IF NOT EXISTS persona_lab (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    pnombre   VARCHAR(100),
    edad      INT,
    ubicacion VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS persona_tmp (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    pnombre     VARCHAR(100),
    edad        VARCHAR(3),
    ubicacion   VARCHAR(100),
    estado      VARCHAR(10) NOT NULL,
    observacion VARCHAR(500),
    id_cargue   BIGINT NOT NULL,
    CONSTRAINT persona_tmp_check CHECK (estado IN ('CARGADO','VALIDADO','PROCESADO','ERROR'))
);

-- 1) CARGUE: copia la fila del Excel sin alterarla
DROP PROCEDURE IF EXISTS CARGAR_DATOS_EXCEL;
DELIMITER //
CREATE PROCEDURE CARGAR_DATOS_EXCEL(
    IN pnombre   VARCHAR(100),
    IN edad      VARCHAR(100),
    IN ubicacion VARCHAR(100),
    IN idCargue  BIGINT
)
BEGIN
    INSERT INTO persona_tmp (pnombre, edad, ubicacion, estado, observacion, id_cargue)
    VALUES (pnombre, edad, ubicacion, 'CARGADO', '', idCargue);
END //
DELIMITER ;

-- 2) VALIDACION (reglas del laboratorio: nombre/edad/ubicacion no vacios, edad numerica de 18 a 35)
DROP PROCEDURE IF EXISTS VALIDAR_DATOS_CARGUE;
DELIMITER //
CREATE PROCEDURE VALIDAR_DATOS_CARGUE(IN idCargue BIGINT)
BEGIN
    UPDATE persona_tmp
       SET estado = 'ERROR', observacion = CONCAT(observacion, ' El campo Nombre no puede ser vacio.')
     WHERE id_cargue = idCargue AND estado = 'CARGADO'
       AND (pnombre IS NULL OR TRIM(pnombre) = '');

    UPDATE persona_tmp
       SET estado = 'ERROR', observacion = CONCAT(observacion, ' El campo Edad no puede ser vacio.')
     WHERE id_cargue = idCargue AND estado = 'CARGADO'
       AND (edad IS NULL OR TRIM(edad) = '');

    UPDATE persona_tmp
       SET estado = 'ERROR', observacion = CONCAT(observacion, ' El campo Edad no cumple con las especificaciones numericas.')
     WHERE id_cargue = idCargue AND estado = 'CARGADO'
       AND edad IS NOT NULL AND TRIM(edad) <> '' AND edad NOT REGEXP '^[0-9]{1,3}$';

    UPDATE persona_tmp
       SET estado = 'ERROR', observacion = CONCAT(observacion, ' El campo Edad no cumple con el rango de valores parametrizados de 18 a 35.')
     WHERE id_cargue = idCargue AND estado = 'CARGADO'
       AND edad REGEXP '^[0-9]{1,3}$'
       AND (CONVERT(edad, SIGNED INTEGER) < 18 OR CONVERT(edad, SIGNED INTEGER) > 35);

    UPDATE persona_tmp
       SET estado = 'ERROR', observacion = CONCAT(observacion, ' El campo Ubicacion no puede ser vacio.')
     WHERE id_cargue = idCargue AND estado = 'CARGADO'
       AND (ubicacion IS NULL OR TRIM(ubicacion) = '');

    UPDATE persona_tmp
       SET estado = 'VALIDADO'
     WHERE id_cargue = idCargue AND estado = 'CARGADO'
       AND (observacion IS NULL OR observacion = '');
END //
DELIMITER ;

-- 3) PROCESADO: pasa los VALIDADO a la tabla transaccional y los marca PROCESADO
DROP PROCEDURE IF EXISTS PROCESAR_DATOS_EXCEL;
DELIMITER //
CREATE PROCEDURE PROCESAR_DATOS_EXCEL(IN idCargue BIGINT)
BEGIN
    INSERT INTO persona_lab (pnombre, edad, ubicacion)
    SELECT pnombre, CONVERT(edad, SIGNED INTEGER), ubicacion
      FROM persona_tmp
     WHERE id_cargue = idCargue AND estado = 'VALIDADO';

    UPDATE persona_tmp SET estado = 'PROCESADO'
     WHERE id_cargue = idCargue AND estado = 'VALIDADO';
END //
DELIMITER ;
