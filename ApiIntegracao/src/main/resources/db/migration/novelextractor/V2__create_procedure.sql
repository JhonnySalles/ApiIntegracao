DELIMITER $$

CREATE DEFINER=`admin`@`%` PROCEDURE `create_table`(IN _tablename VARCHAR(100))
BEGIN

	SET @sql = CONCAT('CREATE TABLE IF NOT EXISTS ',_tablename,'_volumes (
	  id VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
	  novel VARCHAR(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
	  serie VARCHAR(250) DEFAULT NULL,
	  titulo VARCHAR(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
	  titulo_alternativo VARCHAR(250) DEFAULT NULL,
	  descricao LONGTEXT,
	  arquivo VARCHAR(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
	  editora VARCHAR(250) DEFAULT NULL,
	  autor VARCHAR(900) DEFAULT NULL,
	  volume FLOAT DEFAULT NULL,
	  linguagem VARCHAR(4) DEFAULT NULL,
	  is_favorito TINYINT(1) DEFAULT "0",
	  is_processado TINYINT(1) DEFAULT "0",
	  atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP,
	  PRIMARY KEY (id)
	) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('CREATE TABLE IF NOT EXISTS ',_tablename,'_capas (
          id VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
          id_volume VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
          novel VARCHAR(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
          volume FLOAT NOT NULL,
          linguagem VARCHAR(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
          arquivo VARCHAR(250) DEFAULT NULL,
          extensao VARCHAR(10) DEFAULT NULL,
          capa LONGBLOB,
          atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (id),
          KEY ',_tablename,'_volumes_fk (id_volume),
          CONSTRAINT ',_tablename,'_volumes_capas_fk FOREIGN KEY (id_volume) REFERENCES ',_tablename,'_volumes (id) ON DELETE CASCADE ON UPDATE CASCADE
        ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('CREATE TABLE IF NOT EXISTS ',_tablename,'_capitulos (
          id VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
          id_volume VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
          novel VARCHAR(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
          volume FLOAT NOT NULL,
          capitulo VARCHAR(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
          descricao VARCHAR(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
          sequencia INT DEFAULT NULL,
          linguagem VARCHAR(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
          atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (id),
          KEY ',_tablename,'_volumes_fk (id_volume),
          CONSTRAINT ',_tablename,'_volumes_capitulos_fk FOREIGN KEY (id_volume) REFERENCES ',_tablename,'_volumes (id) ON DELETE CASCADE ON UPDATE CASCADE
        ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('CREATE TABLE IF NOT EXISTS ',_tablename,'_textos (
          id VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
          id_capitulo VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
          sequencia INT DEFAULT NULL,
          texto LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
          atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (id),
          KEY ',_tablename,'_capitulos_fk (id_capitulo),
          CONSTRAINT ',_tablename,'_capitulos_textos_fk FOREIGN KEY (id_capitulo) REFERENCES ',_tablename,'_capitulos (id) ON DELETE CASCADE ON UPDATE CASCADE
        ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('CREATE TABLE IF NOT EXISTS ',_tablename,'_vocabularios (
          id_vocabulario VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
          id_volume VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
          id_capitulo VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
          atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP,
          KEY ',_tablename,'_vocab_volume_fk (id_volume),
          KEY ',_tablename,'_vocab_capitulo_fk (id_capitulo),
          KEY ',_tablename,'_vocab_vocabulario_fk (id_vocabulario),
          CONSTRAINT ',_tablename,'_vocab_vocabulario_fk FOREIGN KEY (id_vocabulario) REFERENCES _vocabularios (id),
          CONSTRAINT ',_tablename,'_vocab_capitulo_fk FOREIGN KEY (id_capitulo) REFERENCES ',_tablename,'_capitulos (id),
          CONSTRAINT ',_tablename,'_vocab_volume_fk FOREIGN KEY (id_volume) REFERENCES ',_tablename,'_volumes (id)
        ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$


CREATE DEFINER=`admin`@`%` PROCEDURE `drop_table`(IN _tablename VARCHAR(100))
BEGIN

    SET @sql = CONCAT('DROP TABLE IF EXISTS ',_tablename,'_vocabularios;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('DROP TABLE IF EXISTS ',_tablename,'_textos;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('DROP TABLE IF EXISTS ',_tablename,'_paginas;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('DROP TABLE IF EXISTS ',_tablename,'_capitulos;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('DROP TABLE IF EXISTS ',_tablename,'_capas;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('DROP TABLE IF EXISTS ',_tablename,'_volumes;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

END$$


CREATE DEFINER=`admin`@`%` PROCEDURE `delete_volume`(IN _tablename VARCHAR(100), _IdsVolume VARCHAR(900))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    ROLLBACK;
    RESIGNAL;
    END;

    START TRANSACTION;
    SET @sql = CONCAT('DELETE vol FROM ',_tablename,'_vocabularios AS vol INNER JOIN ',_tablename, "_capitulos AS c ON c.id = vol.id_capitulo INNER JOIN ", _tablename, "_volumes AS v ON v.id = c.id_volume WHERE v.id IN (", _IdsVolume, ");");
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;


    SET @sql = CONCAT('DELETE vol FROM ',_tablename,'_vocabularios AS vol INNER JOIN ',_tablename, "_volumes AS v ON v.id = vol.id_volume WHERE v.id IN (", _IdsVolume, ");");
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;


    SET @sql = CONCAT('DELETE t FROM ',_tablename,'_textos AS t INNER JOIN  ',_tablename, "_capitulos AS c ON c.id = t.id_capitulo INNER JOIN ",
          _tablename, "_volumes AS v ON v.id = c.id_volume WHERE v.id IN (", _IdsVolume, ");");
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;


    SET @sql = CONCAT('DELETE c FROM ',_tablename,'_capitulos AS c INNER JOIN ',_tablename, "_volumes AS v ON v.id = c.id_volume WHERE v.id IN (", _IdsVolume, ");");
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @sql = CONCAT('DELETE v FROM ',_tablename,'_volumes AS v WHERE v.id IN (', _IdsVolume, ");");
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    COMMIT;

END$$


CREATE DEFINER=`admin`@`%` PROCEDURE `delete_capitulos`(IN _tablename VARCHAR(100), _IdsCapitulo VARCHAR(900))
BEGIN

    DECLARE done BOOLEAN DEFAULT FALSE;
    DECLARE id VARCHAR(36);
    DECLARE cur CURSOR FOR SELECT Caps FROM viewcap;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        DROP VIEW IF EXISTS viewcap;
        ROLLBACK;
        RESIGNAL;
    END;

    DROP VIEW IF EXISTS viewcap;

    SET @sql = CONCAT('CREATE VIEW viewcap AS SELECT Cap.ID AS Caps FROM ',_tablename,'_capitulos AS reg INNER JOIN ',_tablename, "_capitulos AS cap ON reg.id_volume = cap.id_volume ",
            " AND (cap.capitulo >= reg.capitulo OR cap.is_extra = 1) WHERE reg.id IN (", _IdsCapitulo, ") ORDER BY cap.is_Extra, cap.Capitulo;");
    PREPARE stmt FROM @sql;
    EXECUTE stmt;


    SET @cnt = FOUND_ROWS();
    IF @cnt > 0 THEN
        START TRANSACTION;
        OPEN cur;
            read_loop: LOOP
                FETCH cur INTO id;
                IF done THEN
                    LEAVE read_loop;
                END IF;

                SET @sql = CONCAT('DELETE vol FROM ',_tablename,'_vocabularios AS vol INNER JOIN ',_tablename, "_paginas AS p ON p.id = vol.id_pagina INNER JOIN ",
                            _tablename, "_capitulos AS c ON c.id = p.id_capitulo WHERE c.id IN (", '"', id, '"', ");");
                PREPARE stmt FROM @sql;
                EXECUTE stmt;
                DEALLOCATE PREPARE stmt;

                SET @sql = CONCAT('DELETE vol FROM ',_tablename,'_vocabularios AS vol INNER JOIN ',_tablename, "_capitulos AS c ON c.id = vol.id_capitulo WHERE c.id IN (", '"', id, '"', ");");
                PREPARE stmt FROM @sql;
                EXECUTE stmt;
                DEALLOCATE PREPARE stmt;


                SET @sql = CONCAT('DELETE t FROM ',_tablename,'_textos AS t INNER JOIN  ',_tablename, "_paginas AS p ON p.id = t.id_pagina INNER JOIN ",
                            _tablename, "_capitulos AS c ON c.id = p.id_capitulo WHERE c.id IN (", '"', id, '"', ");");
                PREPARE stmt FROM @sql;
                EXECUTE stmt;
                DEALLOCATE PREPARE stmt;

                SET @sql = CONCAT('DELETE p FROM ',_tablename,'_paginas p INNER JOIN ',_tablename, "_capitulos AS c ON c.id = p.id_capitulo WHERE c.id IN (", '"', id, '"', ");");
                PREPARE stmt FROM @sql;
                EXECUTE stmt;
                DEALLOCATE PREPARE stmt;


                SET @sql = CONCAT('DELETE c FROM ',_tablename,'_capitulos AS c WHERE c.id IN (', '"', id, '"', ");");
                PREPARE stmt FROM @sql;
                EXECUTE stmt;
                DEALLOCATE PREPARE stmt;

            END LOOP read_loop;

        CLOSE cur;
        COMMIT;
    END IF;

    DROP VIEW IF EXISTS viewcap;

END$$


CREATE DEFINER=`admin`@`%` PROCEDURE `vocabulary_exists`(IN _tablename VARCHAR(255), IN _name VARCHAR(255))
BEGIN
    SELECT Table_Name AS Tabela
    FROM information_schema.tables
    WHERE table_schema = _tablename
      AND Table_Name LIKE '%_vocabularios%'
      AND Table_Name LIKE '%%%s%%'
    GROUP BY Tabela;
END$$


CREATE DEFINER=`admin`@`%` PROCEDURE `list_tables`(IN _tablename VARCHAR(255))
BEGIN
    SELECT REPLACE(Table_Name, '_volumes', '') AS Tabela
    FROM information_schema.tables
    WHERE table_schema = _tablename AND Table_Name LIKE '%_volumes%';
END$$

DELIMITER ;