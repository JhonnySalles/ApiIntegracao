CREATE TABLE IF NOT EXISTS consultas (
    id VARCHAR(36) NOT NULL,
    computador VARCHAR(250),
    ip VARCHAR(250),
    consulta DATETIME,
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS conexoes (
    id INT NOT NULL AUTO_INCREMENT,
    tipo ENUM ('API','MANGA_EXTRACTOR','NOVEL_EXTRACTOR','TEXTO_INGLES','TEXTO_JAPONES','DECKSUBTITLE','PROCESSA_TEXTO','FIREBASE') CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'API',
    url VARCHAR(250) DEFAULT NULL,
    username VARCHAR(250) DEFAULT NULL,
    password VARCHAR(250) DEFAULT NULL,
    base VARCHAR(100) DEFAULT NULL,
    driver ENUM ('MYSQL','POSTGRE') CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'MYSQL',
    mapeamento ENUM ('JPA','JDBC','AMBOS') CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'JDBC',
    ativo BOOLEAN DEFAULT FALSE,
    PRIMARY KEY ( id ),
    UNIQUE KEY tipo ( tipo )
);

INSERT INTO conexoes (id, tipo, url, username, password, base, driver, mapeamento, ativo)
VALUES (1, 'API', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'apiintegracao', 'MYSQL', 'JPA', TRUE),
       (2, 'TEXTO_JAPONES', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'texto_japones', 'MYSQL', 'JPA', TRUE),
       (3, 'TEXTO_INGLES', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'texto_ingles', 'MYSQL', 'JPA', TRUE),
       (4, 'MANGA_EXTRACTOR', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'manga_extractor', 'MYSQL', 'JDBC', TRUE),
       (5, 'NOVEL_EXTRACTOR', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'novel_extractor', 'MYSQL', 'JDBC', TRUE),
       (6, 'DECKSUBTITLE', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'decksubtitle', 'MYSQL', 'JDBC', TRUE),
       (7, 'PROCESSA_TEXTO', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'processa_texto', 'MYSQL', 'AMBOS', TRUE);
