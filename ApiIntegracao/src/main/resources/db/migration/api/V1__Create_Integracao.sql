CREATE TABLE IF NOT EXISTS consultas (
    id VARCHAR (36) NOT NULL,
    computador VARCHAR (250),
    ip VARCHAR (250),
    consulta DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS conexoes (
    id       INT NOT NULL AUTO_INCREMENT,
    tipo     enum('API','MANGA_EXTRACTOR','NOVEL_EXTRACTOR','TEXTO_INGLES','TEXTO_JAPONES','DECKSUBTITLE','FIREBASE') CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'API',
    url      VARCHAR(250) DEFAULT NULL,
    username VARCHAR(250) DEFAULT NULL,
    password VARCHAR(250) DEFAULT NULL,
    base     VARCHAR(100) DEFAULT NULL,
    driver   enum('MYSQL','POSTGRE') CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'MYSQL',
    ativo    BOOLEAN DEFAULT false,
    PRIMARY KEY ( id ),
    UNIQUE KEY tipo (tipo)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

INSERT INTO conexoes (id, tipo, url, username, password, base, driver, ativo) VALUES
(1, 'API', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'apiintegracao', 'MYSQL', true),
(2, 'TEXTO_JAPONES', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'texto_japones', 'MYSQL', true),
(3, 'TEXTO_INGLES', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'texto_ingles', 'MYSQL', true),
(4, 'MANGA_EXTRACTOR', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'manga_extractor', 'MYSQL', true),
(5, 'NOVEL_EXTRACTOR', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'novel_extractor', 'MYSQL', true),
(6, 'DECKSUBTITLE', 'jdbc:mysql://localhost:3306', 'admin', 'admin', 'decksubtitle', 'MYSQL', true);
